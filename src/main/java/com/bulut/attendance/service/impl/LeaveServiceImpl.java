package com.bulut.attendance.service.impl;

import com.bulut.attendance.domain.Leave;
import com.bulut.attendance.repository.LeaveRepository;
import com.bulut.attendance.repository.search.LeaveSearchRepository;
import com.bulut.attendance.service.LeaveService;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bulut.attendance.domain.Leave}.
 */
@Service
@Transactional
public class LeaveServiceImpl implements LeaveService {

    private final Logger log = LoggerFactory.getLogger(LeaveServiceImpl.class);

    private final LeaveRepository leaveRepository;

    private final LeaveSearchRepository leaveSearchRepository;

    public LeaveServiceImpl(LeaveRepository leaveRepository, LeaveSearchRepository leaveSearchRepository) {
        this.leaveRepository = leaveRepository;
        this.leaveSearchRepository = leaveSearchRepository;
    }

    @Override
    public Leave save(Leave leave) {
        log.debug("Request to save Leave : {}", leave);
        leave = leaveRepository.save(leave);
        leaveSearchRepository.index(leave);
        return leave;
    }

    @Override
    public Leave update(Leave leave) {
        log.debug("Request to update Leave : {}", leave);
        leave = leaveRepository.save(leave);
        leaveSearchRepository.index(leave);
        return leave;
    }

    @Override
    public Optional<Leave> partialUpdate(Leave leave) {
        log.debug("Request to partially update Leave : {}", leave);

        return leaveRepository
            .findById(leave.getId())
            .map(existingLeave -> {
                if (leave.getStart() != null) {
                    existingLeave.setStart(leave.getStart());
                }
                if (leave.getEnd() != null) {
                    existingLeave.setEnd(leave.getEnd());
                }
                if (leave.getBpmsApproveStatus() != null) {
                    existingLeave.setBpmsApproveStatus(leave.getBpmsApproveStatus());
                }

                return existingLeave;
            })
            .map(leaveRepository::save)
            .map(savedLeave -> {
                leaveSearchRepository.index(savedLeave);
                return savedLeave;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Leave> findAll(Pageable pageable) {
        log.debug("Request to get all Leaves");
        return leaveRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Leave> findOne(UUID id) {
        log.debug("Request to get Leave : {}", id);
        return leaveRepository.findById(id);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Leave : {}", id);
        leaveRepository.deleteById(id);
        leaveSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Leave> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Leaves for query {}", query);
        return leaveSearchRepository.search(query, pageable);
    }
}
