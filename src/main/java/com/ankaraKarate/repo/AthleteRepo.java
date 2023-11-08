package com.ankaraKarate.repo;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ankaraKarate.entity.Athlete;
@Repository
public interface AthleteRepo extends JpaRepository<Athlete, Long> {

	Page<Athlete> findAllByName(String keyword, Pageable pageable);

	Page<Athlete> findAllByNameLike(String keyword, Pageable pageable);

	Page<Athlete> findAllByNameContainingIgnoreCase(String keyword, Pageable pageable);


}
