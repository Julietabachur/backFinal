package com.backendIntegrador.service;

import com.backendIntegrador.model.Policy;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IPolicyService {

    Policy save( Policy policy ) throws Exception;

    boolean delete( String id ) throws Exception;

    List<Policy> findAllPolicies() throws Exception;

    Policy getPolicyById( String id ) throws Exception;

    @Transactional
    Policy getPolicyByPolicyName( String policyName );

    boolean checkPolicyName( String policyName );

    Policy update( Policy policy ) throws Exception;
}
