package com.aionos.agent.repository;

import com.aionos.agent.entity.Escalation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EscalationRepository extends JpaRepository<Escalation, Long> {
    List<Escalation> findAllByOrderByCreatedAtDesc();
    List<Escalation> findByPnrOrderByCreatedAtDesc(String pnr);
}
