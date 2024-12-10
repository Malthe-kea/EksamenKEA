package org.example.eksamenkea.service;

import org.example.eksamenkea.model.Subproject;
import org.example.eksamenkea.repository.interfaces.ISubprojectRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SubprojectService {
    private final ISubprojectRepository iSubprojectRepository;

    public SubprojectService(ApplicationContext context, @Value("ISUBPROJECTREPOSITORY") String impl) {
        this.iSubprojectRepository = (ISubprojectRepository) context.getBean(impl);
    }

    public Subproject getSubprojectBySubprojectId(int subprojectId) throws Errorhandling {
        return iSubprojectRepository.getSubprojectBySubprojectId(subprojectId);
    }

    public void updateSubproject(Subproject subproject) throws Errorhandling {
        iSubprojectRepository.updateSubproject(subproject);
    }

    public List<Subproject> getSubjectsByProjectId(int projectId) throws Errorhandling {
        return iSubprojectRepository.getSubjectsByProjectId(projectId);
    }



}
