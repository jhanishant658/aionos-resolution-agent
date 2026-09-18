package com.aionos.agent.service;

import com.aionos.agent.entity.ActionLog;
import com.aionos.agent.enums.ActionStatus;
import com.aionos.agent.enums.ActionType;
import com.aionos.agent.repository.ActionLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {

    private static final Logger log = LoggerFactory.getLogger(AuditLogService.class);
    private final ActionLogRepository actionLogRepository;

    public AuditLogService(ActionLogRepository actionLogRepository) {
        this.actionLogRepository = actionLogRepository;
    }

    public ActionLog logAction(Long conversationId, String pnr, String customerName,
                               ActionType actionType, ActionStatus status, String reason, String policyReference) {
        ActionLog actionLog = ActionLog.builder()
                .conversationId(conversationId)
                .pnr(pnr)
                .customerName(customerName)
                .actionType(actionType)
                .status(status)
                .reason(reason)
                .policyReference(policyReference)
                .timestamp(LocalDateTime.now())
                .build();

        log.info("[AUDIT LOG] PNR: {} | Customer: {} | Action: {} | Status: {} | Policy: {}",
                pnr, customerName, actionType, status, policyReference);

        return actionLogRepository.save(actionLog);
    }

    public List<ActionLog> getLogsByConversation(Long conversationId) {
        return actionLogRepository.findByConversationIdOrderByTimestampDesc(conversationId);
    }

    public List<ActionLog> getLogsByPnr(String pnr) {
        return actionLogRepository.findByPnrOrderByTimestampDesc(pnr);
    }

    public List<ActionLog> getAllLogs() {
        return actionLogRepository.findAllByOrderByTimestampDesc();
    }
}