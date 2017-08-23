package com.zachcalvert.PictureScript.repository;

import com.zachcalvert.PictureScript.model.File;
import org.springframework.data.repository.CrudRepository;

public interface FileRepository extends CrudRepository<File, Long> {
}
