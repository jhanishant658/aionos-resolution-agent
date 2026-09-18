package com.aionos.agent.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "policy_rules")
public class PolicyRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String ruleCode;

    @Column(nullable = false)
    private String ruleName;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false, length = 1000)
    private String conditionDescription;

    @Column(nullable = false, length = 1000)
    private String allowedAction;

    @Column(length = 1000)
    private String escalationCondition;

    public PolicyRule() {}

    public PolicyRule(Long id, String ruleCode, String ruleName, String category,
                      String conditionDescription, String allowedAction, String escalationCondition) {
        this.id = id;
        this.ruleCode = ruleCode;
        this.ruleName = ruleName;
        this.category = category;
        this.conditionDescription = conditionDescription;
        this.allowedAction = allowedAction;
        this.escalationCondition = escalationCondition;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String ruleCode;
        private String ruleName;
        private String category;
        private String conditionDescription;
        private String allowedAction;
        private String escalationCondition;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder ruleCode(String ruleCode) { this.ruleCode = ruleCode; return this; }
        public Builder ruleName(String ruleName) { this.ruleName = ruleName; return this; }
        public Builder category(String category) { this.category = category; return this; }
        public Builder conditionDescription(String conditionDescription) { this.conditionDescription = conditionDescription; return this; }
        public Builder allowedAction(String allowedAction) { this.allowedAction = allowedAction; return this; }
        public Builder escalationCondition(String escalationCondition) { this.escalationCondition = escalationCondition; return this; }
        public PolicyRule build() {
            return new PolicyRule(id, ruleCode, ruleName, category, conditionDescription, allowedAction, escalationCondition);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRuleCode() { return ruleCode; }
    public void setRuleCode(String ruleCode) { this.ruleCode = ruleCode; }

    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getConditionDescription() { return conditionDescription; }
    public void setConditionDescription(String conditionDescription) { this.conditionDescription = conditionDescription; }

    public String getAllowedAction() { return allowedAction; }
    public void setAllowedAction(String allowedAction) { this.allowedAction = allowedAction; }

    public String getEscalationCondition() { return escalationCondition; }
    public void setEscalationCondition(String escalationCondition) { this.escalationCondition = escalationCondition; }
}