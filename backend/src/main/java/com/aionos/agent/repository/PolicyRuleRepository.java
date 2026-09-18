package com.aionos.agent.repository;

import com.aionos.agent.entity.PolicyRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface PolicyRuleRepository extends JpaRepository<PolicyRule, Long> {
    Optional<PolicyRule> findByRuleCode(String ruleCode);
    List<PolicyRule> findByCategory(String category);
}
