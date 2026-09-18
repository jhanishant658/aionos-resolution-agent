package com.aionos.agent.repository;

import com.aionos.agent.entity.ActionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {
    List<ActionLog> findByConversationIdOrderByTimestampDesc(Long conversationId);
    List<ActionLog> findByPnrOrderByTimestampDesc(String pnr);
    List<ActionLog> findAllByOrderByTimestampDesc();
}
