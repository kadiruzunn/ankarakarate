package com.ankaraKarate.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ankaraKarate.entity.Athlete;
import com.ankaraKarate.service.AthleteService;
import com.ankaraKarate.service.CategoryService;
import com.ankaraKarate.service.FileStorageService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AthleteController {
	
private final AthleteService athleteService;
private final CategoryService categoryService;
private final FileStorageService fileService;
	
	static final String SPORCULAR = "sporcular";
	static final String REDIRECT_SPORCULAR = "redirect:/sporcular?pageNo=1&recordCount=10&sortBy=id&direction=asc&keyword=";
	static final String SPORCU_DETAY = "sporcudetay";
	static final String YENI_SPORCU = "yenisporcu";
	static final String SPORCU_GUNCELLE = "sporcuguncelle";
	

	
	@GetMapping("/sporcuguncelle/{id}")
	public String sporcuguncelle(@PathVariable Long id, Model model) {

		Athlete guncellenecekSporcu = athleteService.idIleSporcuGetir(id);

		Map<Integer, String> kategoriler = categoryService.kategoriMap();

		model.addAttribute("kategoriler", kategoriler);
		model.addAttribute("sporcu", guncellenecekSporcu);
		model.addAttribute("secilenKategori", guncellenecekSporcu.getId());

		return SPORCU_GUNCELLE;
	}
	
	@PostMapping("/sporcuguncelle/{id}")
	public String sporcuguncelle(@PathVariable Long id, @Valid @ModelAttribute Athlete athlete, BindingResult result,
			Model model, @RequestParam("img") MultipartFile img) {

		if (result.hasErrors()) {

			Map<Integer, String> kategoriler = categoryService.kategoriMap();

			model.addAttribute("kategoriler", kategoriler);
			model.addAttribute("sporcu", athlete);
			model.addAttribute("secilenKategori", athlete.getId());

			return SPORCU_GUNCELLE;
		}
		
		if(img.getOriginalFilename() != null) {
			try {
				fileService.save(img);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			athlete.setImage(img.getOriginalFilename());
		}
		
		athleteService.guncelle(id, athlete);
		
		return REDIRECT_SPORCULAR;
	}

	@GetMapping("/search")
	public String ara(String keyword) {
		return REDIRECT_SPORCULAR + keyword; 
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/sporcular")
	public String sporcular(Model model, @RequestParam Integer pageNo, @RequestParam Integer recordCount, 
			@RequestParam String sortBy,@RequestParam String direction, String keyword ) {
		pageNo = pageNo == null ? 1 : pageNo;
		recordCount = recordCount == null ? 10 : recordCount;
		sortBy = sortBy.isEmpty() ? "id" : sortBy;
		direction = direction.isEmpty()? "asc" : direction;
		
	
		model.addAttribute("nameLabel", "Adı");
		
		if("name".equals(sortBy)) {
			if("desc".equals(direction)) {
				model.addAttribute("nameLabel", "Adı ^");
				model.addAttribute("nameSortDirection", "asc");
			}else {
				model.addAttribute("nameLabel", "Adı v");
				model.addAttribute("nameSortDirection", "desc");
			}
		}
		
		
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("recordCount", recordCount);
		
		Page<Athlete> page = athleteService.sporcular(pageNo, recordCount, sortBy, direction, keyword);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		
		model.addAttribute("sortBy", sortBy);
		model.addAttribute("direction", direction);
		
		List<Athlete> liste = page.getContent(); //productService.urunler(pageNo, recordCount, sortBy, direction);
		model.addAttribute("sporcular", liste);
		return SPORCULAR;
	}

	@GetMapping("/sporcusil/{id}")
	public String sporcuSil(@PathVariable Long id)
	{
		athleteService.sil(id);
		return REDIRECT_SPORCULAR;
	}
	
	@GetMapping("/sporcudetay/{id}")
	public String sporcuDetay(@PathVariable Long id, Model model)
	{
		Athlete a = athleteService.idIleSporcuGetir(id);
		
		model.addAttribute("sporcu", a);
		return SPORCU_DETAY;
	}

	@GetMapping("/yenisporcu")
	public String yeniSporcu(Model model)
	{
		Map<Integer, String> kategoriler = categoryService.kategoriMap();

		model.addAttribute("kategoriler", kategoriler);
		model.addAttribute("sporcu", new Athlete());
		
		return YENI_SPORCU;
	}

	@PostMapping("/yenisporcu")
	public String yeniSporcuKaydet(@Valid @ModelAttribute("urun") Athlete athlete, BindingResult result,
			@RequestParam("img") MultipartFile img, Model model) {
		if (result.hasErrors()) {
			Map<Integer, String> kategoriler = categoryService.kategoriMap();

			model.addAttribute("kategoriler", kategoriler);
			athlete.setImage(img.getOriginalFilename());
			model.addAttribute("img", img);
			model.addAttribute("sporcu", athlete);
			model.addAttribute("secilenKategori", athlete.getId());
			return YENI_SPORCU;
		}

		try {
			fileService.save(img);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String resim = athlete.getImage();

		athlete.setImage(resim.substring(resim.lastIndexOf("\\") + 1));

		athleteService.kaydet(athlete);

		return REDIRECT_SPORCULAR;
	}

	@GetMapping("img/{fileName:.+}")
	public ResponseEntity<Resource> getImage(@PathVariable String fileName) {

		Resource file = fileService.load(fileName);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

}
