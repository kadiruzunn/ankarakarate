package com.ankaraKarate.service;

import com.ankaraKarate.dto.EmailDetails;

public interface EmailService {
	void send(EmailDetails details);
}