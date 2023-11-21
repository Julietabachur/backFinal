package com.backendIntegrador.service.impl;

import com.backendIntegrador.model.Category;
import com.backendIntegrador.model.Characteristic;
import com.backendIntegrador.model.Policy;
import com.backendIntegrador.repository.CategoryRepository;
import com.backendIntegrador.repository.PolicyRepository;
import com.backendIntegrador.service.IPolicyService;
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
public class PolicyService implements IPolicyService {

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private Validator validator;

    @Override
    @Transactional
    public Policy save(Policy policy) throws Exception {
        try {
            Set<ConstraintViolation<Policy>> violations = validator.validate(policy);
            if (!violations.isEmpty()) {
                // Handle validation errors
                StringBuilder errorMessage = new StringBuilder("Validation errors: ");
                for (ConstraintViolation<Policy> violation : violations) {
                    errorMessage.append(violation.getMessage()).append(", ");
                }
                throw new Exception(errorMessage.toString());
            }
            Policy existingPolicy = policyRepository.findByPolicyName(policy.getPolicyName());
            if (existingPolicy != null) {
                // La politica ya existe, puedes manejarlo según tus necesidades, como lanzar una excepción o actualizar la politica existente.
                // Por ejemplo, para lanzar una excepción, puedes hacer lo siguiente:
                throw new Exception("La politica ya existe en la base de datos.");
            } else {
                // La politica no existe, puedes guardarla y retornarla.
                policyRepository.save(policy);
                return policy;
            }
        } catch (Exception e) {
            // Maneja la excepción de manera apropiada, por ejemplo, registrándola o lanzando una excepción personalizada.
            throw new Exception("Error al guardar la politica: " + e.getMessage());
        }
    }

    @Override
    public boolean delete(String id) throws Exception {
        try {
            if (policyRepository.existsById(id)) {
                policyRepository.deleteById(id);
                return true;
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return false;
    }

    @Override
    public Page<Policy> findAll(Pageable pageable) throws Exception {
        try {
            return policyRepository.findAll(pageable);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<Policy> findAllPolicies() throws Exception {
        return policyRepository.findAll();
    }

    @Override
    public Policy getPolicyById(String id) throws Exception {
        try {
            return policyRepository.findById(id).orElse(null);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Policy getPolicyByPolicyName(String policyName) {
        return policyRepository.findByPolicyName(policyName);
    }

    @Override
    public boolean checkPolicyName(String policyName) {
        Policy existingPolicy = policyRepository.findByPolicyName(policyName);
        return existingPolicy == null;
    }

    @Override
    public Policy update(Policy policy) throws Exception {
        try {
            Set<ConstraintViolation<Policy>> violations = validator.validate(policy);
            if (!violations.isEmpty()) {
                // Handle validation errors
                StringBuilder errorMessage = new StringBuilder("Validation errors: ");
                for (ConstraintViolation<Policy> violation : violations) {
                    errorMessage.append(violation.getMessage()).append(", ");
                }
                throw new Exception(errorMessage.toString());
            }
            Policy existingPolicyByName = policyRepository.findByPolicyName(policy.getPolicyName());
            Policy existingPolicy = policyRepository.findById(policy.getId()).orElse(null);
            if (existingPolicy != null && existingPolicyByName != null) {
                if(!(existingPolicyByName.getId().equals(existingPolicy.getId()))){
                    throw new RuntimeException("Ya existe una politica con ese nombre");
                }
            }
            if (existingPolicy != null) {
                existingPolicy.setPolicyName(policy.getPolicyName());
                existingPolicy.setDescription(policy.getDescription());
                return policyRepository.save(existingPolicy);
            } else {
                throw new RuntimeException("La politica no se encontró para la actualización");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
