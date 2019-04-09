package com.flatter.server.clusteringmicroservice.core.Controllers;

import com.flatter.server.clusteringmicroservice.core.domain.ClusterEvent;
import com.flatter.server.clusteringmicroservice.core.domain.documents.ClusteringDocument;
import com.flatter.server.clusteringmicroservice.core.repositories.ClusteringDocumentRepository;
import com.flatter.server.clusteringmicroservice.core.services.CopyService;
import domain.Questionnaire;
import domain.QuestionnaireableUser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Log4j2
public class UserController {

    private final ApplicationEventPublisher applicationEventPublisher;

    private final ClusteringDocumentRepository clusteringDocumentRepository;

    private final CopyService copyService;

    @Autowired
    public UserController(CopyService copyService, ClusteringDocumentRepository clusteringDocumentRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.copyService = copyService;
        this.clusteringDocumentRepository = clusteringDocumentRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * Called after posting questionnaire before no point
     *
     * @param questionnaire
     * @return
     */
    @PostMapping("/createUser")
    public ResponseEntity createUser(@RequestBody Questionnaire questionnaire) {
        log.error(questionnaire.toString());
        log.error(questionnaire.getUser().toString());

        QuestionnaireableUser questionnaireableUser = copyService.createUserFromDTO(questionnaire);

        ClusteringDocument clusteringDocument = new ClusteringDocument();
        clusteringDocument.setQuestionnaireable(questionnaireableUser);
        clusteringDocumentRepository.save(clusteringDocument);
        applicationEventPublisher.publishEvent(new ClusterEvent(this));
        log.error("Added user: " + clusteringDocument.toString());
        return ResponseEntity.ok(clusteringDocument);
    }

}
