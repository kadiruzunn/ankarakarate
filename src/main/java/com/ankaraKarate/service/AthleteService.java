package com.ankaraKarate.service;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.ankaraKarate.dto.EmailDetails;
import com.ankaraKarate.entity.Athlete;
import com.ankaraKarate.repo.AthleteRepo;




@Service
public class AthleteService {
	
	   @Autowired
		private AthleteRepo athleteRepo;
	   
	   @Autowired
		@Qualifier("gmail")
		private EmailService emailService;

		public List<Athlete> sporcular() {

			return athleteRepo.findAll();

		}

		public void sil(Long id) {
		
			athleteRepo.deleteById(id);
			
		}

		public Athlete idIleSporcuGetir(Long id) {
			
			return athleteRepo.findById(id).get();
			
		}

		public Athlete ekle(Athlete athlete) {
			
			return athleteRepo.save(athlete);
		}
		
		public void kaydet(Athlete athlete) {
			
			athleteRepo.save(athlete);
			
		}
		
		public void guncelle(Long id, @Valid  Athlete athlete) {
			
			Athlete varolanSporcu = idIleSporcuGetir(id);
			
			varolanSporcu.setImage(athlete.getImage());
			varolanSporcu.setName(athlete.getName());
			varolanSporcu.setBirthDate(athlete.getBirthDate());
			varolanSporcu.setBelt(athlete.getBelt());
			varolanSporcu.setWeight(athlete.getWeight());
			varolanSporcu.setWeightType(athlete.getWeightType());
			varolanSporcu.setClub(athlete.getClub());
			varolanSporcu.setTrainer(athlete.getTrainer());
			
			athleteRepo.save(varolanSporcu);
			
		}

public List<Athlete> sporcular(int pageNo, int recordCount) {
	
	Pageable pageable = PageRequest.of(pageNo, recordCount);
	
	return athleteRepo.findAll(pageable).get().toList();
}

public List<Athlete> sporcular(int pageNo, int recordCount, String sortBy) {
	Pageable pageable = PageRequest.of(pageNo, recordCount, Sort.by(sortBy));
	
	return athleteRepo.findAll(pageable).get().toList(); 
}

public Page<Athlete> sporcular(int pageNo, int recordCount, String sortBy, String direction) {
	Pageable pageable = PageRequest.of(pageNo-1, recordCount, Sort.by(("desc".equals(direction) ? Direction.DESC:Direction.ASC),sortBy));
			
			return athleteRepo.findAll(pageable);
		}

public Page<Athlete> sporcular(Integer pageNo, Integer recordCount, String sortBy, String direction, String keyword) {
    Pageable pageable = PageRequest.of(pageNo - 1, recordCount, Sort.by(("desc".equals(direction) ? Direction.DESC : Direction.ASC), sortBy));

    if (StringUtils.isNotBlank(keyword)) {
        return athleteRepo.findAllByNameContainingIgnoreCase(keyword, pageable);
    } else {
        return athleteRepo.findAll(pageable);
    }
}
	
	public void kiloEksilt(Long id) {

		Athlete varolanSporcu = idIleSporcuGetir(id);

		if (varolanSporcu.getWeight() - 1 >= 0) {
			varolanSporcu.setWeight(varolanSporcu.getWeight() - 1);

			athleteRepo.save(varolanSporcu);
			
			
			
			if(varolanSporcu.getWeight() < varolanSporcu.getCriticalWeight()) {
				EmailDetails emailDetails = EmailDetails.builder()
						.to("kadiruzun0202@gmail.com")
						.subject("Kritik Kilo Uyarısı")
						.body(String.format("%s isimli sporcunun kilosu, kritik kilo sınınırı olan %.2f altına düştü.",
								varolanSporcu.getName(), varolanSporcu.getCriticalWeight()))
						.build();
				
				emailService.send(emailDetails);
				
			}
		}
	}

	public void kiloArttır(Long id) {
		Athlete varolanSporcu = idIleSporcuGetir(id);
		varolanSporcu.setWeight(varolanSporcu.getWeight() + 1);

		athleteRepo.save(varolanSporcu);
	}

}
