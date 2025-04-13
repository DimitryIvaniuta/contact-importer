package com.importservice.repository;

import com.importservice.entity.UnimportedContact;
import org.springframework.data.repository.CrudRepository;

public interface UnimportedContactRepository extends CrudRepository<UnimportedContact, Long> {

}
