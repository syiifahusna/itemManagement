package com.itemManagement.repository;

import com.itemManagement.entity.Mailing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailingRepository  extends JpaRepository<Mailing,Long> {
}
