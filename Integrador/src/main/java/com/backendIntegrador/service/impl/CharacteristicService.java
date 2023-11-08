package com.backendIntegrador.service.impl;

import com.backendIntegrador.model.Characteristic;
import com.backendIntegrador.repository.CharacteristicRepository;
import com.backendIntegrador.service.ICharacteristicService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class CharacteristicService implements ICharacteristicService {

    @Autowired
    private CharacteristicRepository characteristicRepository;
    @Autowired
    private Validator validator;


    @Override
    @Transactional
    public Characteristic save( Characteristic characteristic ) throws Exception {
        try {

            Set<ConstraintViolation<Characteristic>> violations = validator.validate(characteristic);

            if (!violations.isEmpty()) {
                // Handle validation errors
                StringBuilder errorMessage = new StringBuilder("Validation errors: ");
                for (ConstraintViolation<Characteristic> violation : violations) {
                    errorMessage.append(violation.getMessage()).append(", ");
                }
                throw new Exception(errorMessage.toString());
            }
            Characteristic existingChar = characteristicRepository.findByCharName(characteristic.getCharName());
            if (existingChar == null) {
                characteristicRepository.save(characteristic);
                return characteristic;
            } else {
                throw new Exception("Ya existe esa caracteristica");
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Page<Characteristic> findAll( Pageable pageable ) throws Exception {
        try {
            return characteristicRepository.findAll(pageable);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<Characteristic> findAllChars() throws Exception {
        return characteristicRepository.findAll();
    }

    @Override
    public Characteristic getCharById( String id ) throws Exception {
        try {
            return characteristicRepository.findById(id).orElse(null);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public boolean delete( String id ) throws Exception {
        try {
            if (characteristicRepository.existsById(id)) {
                characteristicRepository.deleteById(id);
                return true;
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return false;
    }

    @Override
    public Characteristic getCharByCharName( String charName ) {
        return characteristicRepository.findByCharName(charName);
    }

    @Override
    public boolean checkCharName( String charName ) {
        Characteristic existingChar = characteristicRepository.findByCharName(charName);
        return existingChar == null;
    }

    @Override
    public Characteristic update( Characteristic characteristic ) throws Exception {
        try {
            Characteristic existingChar = characteristicRepository.findById(characteristic.getId()).orElse(null);

            if (existingChar != null) {
                existingChar.setCharName(characteristic.getCharName());
                existingChar.setCharValue(characteristic.getCharValue());
                existingChar.setCharIcon(characteristic.getCharIcon());

                return characteristicRepository.save(existingChar);
            } else {
                throw new RuntimeException("La caracteristica no se encontró para la actualización");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
