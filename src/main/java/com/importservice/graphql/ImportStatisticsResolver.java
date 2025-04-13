package com.importservice.graphql;

import com.importservice.dto.ImportStatistics;
import com.importservice.repository.ContactRepository;
import com.importservice.repository.UnimportedContactRepository;
import org.springframework.stereotype.Controller;
import org.springframework.graphql.data.method.annotation.QueryMapping;

@Controller
public class ImportStatisticsResolver {

    private final ContactRepository contactRepository;

    private final UnimportedContactRepository unimportedContactRepository;

    public ImportStatisticsResolver(ContactRepository contactRepository,
                                    UnimportedContactRepository unimportedContactRepository) {
        this.contactRepository = contactRepository;
        this.unimportedContactRepository = unimportedContactRepository;
    }

    @QueryMapping
    public ImportStatistics importStatistics() {
        long importedCount = contactRepository.count();
        long failedCount = unimportedContactRepository.count();
        long totalRecords = importedCount + failedCount;
        return new ImportStatistics(importedCount, failedCount, totalRecords);
    }

}
