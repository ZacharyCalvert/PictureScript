package com.zachcalvert.picturescript.repository;

import com.zachcalvert.picturescript.model.File;
import org.springframework.data.repository.CrudRepository;

public interface FileRepository extends CrudRepository<File, Long> {
}
