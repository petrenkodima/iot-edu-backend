package ru.iot.edu.service;

import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import ru.iot.edu.model.Stand;
import ru.iot.edu.repository.StandRepository;

@Service
public class StandService {
    private final StandRepository standRepository;

    public StandService(StandRepository standRepository) {
        this.standRepository = standRepository;
    }

    /**
     * Удалить стенд по его идентификатору.
     *
     * @param standId идентификатор стенда
     * @throws NotFoundException если стенд не найден
     */
    public void deleteStand(Long standId) throws NotFoundException {
        Stand stand = standRepository.findById(standId)
                .orElseThrow(() -> new NotFoundException("Стенд не найден"));
        standRepository.delete(stand);
    }
}
