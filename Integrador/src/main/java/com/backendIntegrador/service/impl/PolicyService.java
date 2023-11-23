package com.backendIntegrador.service.impl;

<<<<<<< HEAD
import com.backendIntegrador.model.Category;
import com.backendIntegrador.model.Characteristic;
import com.backendIntegrador.model.Policy;
import com.backendIntegrador.repository.CategoryRepository;
=======
import com.backendIntegrador.model.Policy;
>>>>>>> 477f01feefd8ac5de7d015c30cd7594127022bb9
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
<<<<<<< HEAD

    @Autowired
    private Validator validator;

=======
    @Autowired
    private Validator validator;


>>>>>>> 477f01feefd8ac5de7d015c30cd7594127022bb9
    @Override
    @Transactional
    public Policy save(Policy policy) throws Exception {
        try {
<<<<<<< HEAD
            Set<ConstraintViolation<Policy>> violations = validator.validate(policy);
=======

            Set<ConstraintViolation<Policy>> violations = validator.validate(policy);

>>>>>>> 477f01feefd8ac5de7d015c30cd7594127022bb9
            if (!violations.isEmpty()) {
                // Handle validation errors
                StringBuilder errorMessage = new StringBuilder("Validation errors: ");
                for (ConstraintViolation<Policy> violation : violations) {
                    errorMessage.append(violation.getMessage()).append(", ");
                }
                throw new Exception(errorMessage.toString());
            }
            Policy existingPolicy = policyRepository.findByPolicyName(policy.getPolicyName());
<<<<<<< HEAD
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
=======
            if (existingPolicy == null) {
                policyRepository.save(policy);
                return policy;
            } else {
                throw new Exception("Ya existe esa politica");
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
>>>>>>> 477f01feefd8ac5de7d015c30cd7594127022bb9
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
<<<<<<< HEAD
    public Policy getPolicyById(String id) throws Exception {
=======
    public Policy getPolicyById (String id) throws Exception {
>>>>>>> 477f01feefd8ac5de7d015c30cd7594127022bb9
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
<<<<<<< HEAD
=======
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
>>>>>>> 477f01feefd8ac5de7d015c30cd7594127022bb9
    public boolean checkPolicyName(String policyName) {
        Policy existingPolicy = policyRepository.findByPolicyName(policyName);
        return existingPolicy == null;
    }

    @Override
    public Policy update(Policy policy) throws Exception {
        try {
<<<<<<< HEAD
            Set<ConstraintViolation<Policy>> violations = validator.validate(policy);
=======

            Set<ConstraintViolation<Policy>> violations = validator.validate(policy);

>>>>>>> 477f01feefd8ac5de7d015c30cd7594127022bb9
            if (!violations.isEmpty()) {
                // Handle validation errors
                StringBuilder errorMessage = new StringBuilder("Validation errors: ");
                for (ConstraintViolation<Policy> violation : violations) {
                    errorMessage.append(violation.getMessage()).append(", ");
                }
                throw new Exception(errorMessage.toString());
            }
<<<<<<< HEAD
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
=======
            Policy existingPolicy = policyRepository.findById(policy.getId()).orElse(null);
            Policy existingPolicyByName = policyRepository.findByPolicyName(policy.getPolicyName());
            if (existingPolicyByName != null && existingPolicy != null) {
                if (!(existingPolicyByName.getId().equals(existingPolicy.getId()))) {
                    throw new RuntimeException("Ya existe una politica con ese nombre");
                }
            }


            if (existingPolicy != null) {
                existingPolicy.setPolicyName(policy.getPolicyName());
                existingPolicy.setDescription(policy.getDescription());

>>>>>>> 477f01feefd8ac5de7d015c30cd7594127022bb9
                return policyRepository.save(existingPolicy);
            } else {
                throw new RuntimeException("La politica no se encontró para la actualización");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
