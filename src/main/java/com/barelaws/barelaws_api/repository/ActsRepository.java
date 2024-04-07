package com.barelaws.barelaws_api.repository;

import com.barelaws.barelaws_api.entity.ActEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActsRepository extends CrudRepository<ActEntity, String> {

    List<ActEntity> findAllBy();

}
