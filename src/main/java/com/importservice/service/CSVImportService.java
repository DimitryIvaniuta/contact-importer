package com.importservice.service;

import com.importservice.dto.ImportStatistics;
import com.importservice.entity.Contact;
import com.importservice.entity.UnimportedContact;
import com.importservice.repository.ContactRepository;
import com.importservice.repository.UnimportedContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CSVImportService {

    private final ContactRepository contactRepository;
    private final UnimportedContactRepository unimportedContactRepository;

    // Configure batch size (e.g., 100 records per thread)
    private static final int BATCH_SIZE = 100;

    // Thread-safe counters for statistics
    private AtomicInteger totalImported = new AtomicInteger(0);

    private AtomicInteger totalFailed = new AtomicInteger(0);

    public CSVImportService(ContactRepository contactRepository, UnimportedContactRepository unimportedContactRepository) {
        this.contactRepository = contactRepository;
        this.unimportedContactRepository = unimportedContactRepository;
    }

    public CompletableFuture<ImportStatistics> importCSV(MultipartFile file) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            // Parse CSV using header mapping (assumes a header row exists)
            CSVFormat format = CSVFormat.Builder.create(CSVFormat.DEFAULT)
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .setTrim(true).get();

            CSVParser csvParser = format.parse(reader);

            List<CSVRecord> records = csvParser.getRecords();
            int totalRecords = records.size();
            log.info("Total records found: {}", totalRecords);

            // Partition records into batches for multithreaded processing
            for (int i = 0; i < records.size(); i += BATCH_SIZE) {
                int end = Math.min(i + BATCH_SIZE, records.size());
                List<CSVRecord> batch = records.subList(i, end);
                futures.add(processBatch(new ArrayList<>(batch)));
            }

            // When all batch processes complete, return the aggregated statistics
            return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                    .thenApply(voided -> new ImportStatistics(
                            (long) totalImported.get(),
                            (long) totalFailed.get(),
                            (long) totalRecords));

        } catch (Exception e) {
            log.error("Error processing CSV file: {}", e.getMessage());
            // If there is a failure reading the file, return statistics with zero processed records.
            return CompletableFuture.completedFuture(new ImportStatistics((long) totalImported.get(),
                    (long) totalFailed.get(), 0L));
        }
    }

    @Async
    public CompletableFuture<Void> processBatch(List<CSVRecord> batchRecords) {
        for (CSVRecord csvRecord : batchRecords) {
            try {
                Contact contact = mapRecordToContact(csvRecord);
                contactRepository.save(contact);
                totalImported.incrementAndGet();
            } catch (Exception e) {
                log.error("Error importing record: {}", e.getMessage());
                try {
                    UnimportedContact unimportedContact = mapRecordToUnimportedContact(csvRecord, e.getMessage());
                    unimportedContactRepository.save(unimportedContact);
                } catch (Exception ex) {
                    log.error("Error saving unimported record: {}", ex.getMessage());
                }
                totalFailed.incrementAndGet();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    private Contact mapRecordToContact(CSVRecord csvRecord) {
        Contact contact = new Contact();
        contact.setOfficeName(csvRecord.get("Office Name"));
        contact.setUserId(csvRecord.get("User ID"));
        contact.setUserName(csvRecord.get("User Name"));
        contact.setFullName(csvRecord.get("Full Name"));
        contact.setTitle(csvRecord.get("Title"));
        contact.setSalutation(csvRecord.get("Salutation"));
        contact.setEnvelopeLabelSalutation(csvRecord.get("Envelope/Label Salutation"));
        contact.setCompany(csvRecord.get("Company"));
        contact.setFirstName(csvRecord.get("First Name"));
        contact.setMi(csvRecord.get("MI"));
        contact.setLastName(csvRecord.get("Last Name"));
        contact.setSpouseFirstName(csvRecord.get("Spouse First Name"));
        contact.setSpouseMI(csvRecord.get("Spouse MI"));
        contact.setSpouseLastName(csvRecord.get("Spouse Last Name"));
        contact.setBirthday(csvRecord.get("Birthday"));
        contact.setSpouseBirthday(csvRecord.get("Spouse Birthday"));
        contact.setWeddingAnniversary(csvRecord.get("Wedding Anniversary"));
        contact.setExpectedMoveDate(csvRecord.get("Expected Move Date"));
        contact.setTypeOfService(csvRecord.get("Type of Service"));
        contact.setWebsite(csvRecord.get("Website"));
        contact.setOfficePhone(csvRecord.get("Office Phone"));
        contact.setFax(csvRecord.get("Fax"));
        contact.setInteractionNotes(csvRecord.get("Interaction Notes"));
        contact.setFullAddress(csvRecord.get("Full Address"));
        contact.setFullStreetAddress(csvRecord.get("Full Street Address"));
        contact.setStreetNumber(csvRecord.get("Street Number"));
        contact.setStreetName(csvRecord.get("Street Name"));
        contact.setUnit(csvRecord.get("Unit"));
        contact.setAddress2(csvRecord.get("Address 2"));
        contact.setAddress3(csvRecord.get("Address 3"));
        contact.setCity(csvRecord.get("City"));
        contact.setStateProvince(csvRecord.get("State/Province"));
        contact.setZipCodePostalCode(csvRecord.get("ZIP Code/Postal Code"));
        contact.setCountry(csvRecord.get("Country"));
        contact.setAlternateAddressStreetHouseNumber(csvRecord.get("Alternate Address Street/House Number"));
        contact.setAlternateAddressStreetName(csvRecord.get("Alternate Address Street Name"));

        contact.setAlternateAddressUnit(csvRecord.get("Unit"));
        contact.setAlternateAddressCity(csvRecord.get("Alternate Address City"));
        contact.setAlternateAddressStateProvinceTerritory(csvRecord.get("Alternate Address State / Province / Territory"));
        contact.setAlternateAddressZipPostalCode(csvRecord.get("Alternate Address ZIP / Postal Code"));
        contact.setAlternateAddress2(csvRecord.get("Alternate Address 2"));
        contact.setAlternateAddress3(csvRecord.get("Alternate Address 3"));
        contact.setAlternateFullStreetAddress(csvRecord.get("Alternate Full Street Address"));
        contact.setPreferredPhone(csvRecord.get("Preferred Phone"));
        contact.setWorkPhone(csvRecord.get("Work Phone"));
        contact.setHomePhone(csvRecord.get("Home Phone"));
        contact.setCellPhone(csvRecord.get("Cell Phone"));
        contact.setSpouseCellPhone(csvRecord.get("Spouse Cell Phone"));
        contact.setSpouseWorkPhone(csvRecord.get("Spouse Work Phone"));
        contact.setBestTimeToContact(csvRecord.get("Best Time to Contact"));

        contact.setFaxAlt(csvRecord.get("Fax"));
        contact.setEmail(csvRecord.get("Email"));
        contact.setSecondaryEmail(csvRecord.get("Secondary Email"));
        contact.setPartnerEmailAddress(csvRecord.get("Partner Email Address"));
        contact.setDoNotMail(csvRecord.get("Do Not Mail"));
        contact.setOtherPhone(csvRecord.get("Other Phone"));
        contact.setPosition(csvRecord.get("Position"));
        contact.setEmployer(csvRecord.get("Employer"));
        contact.setSpouseEmployer(csvRecord.get("Spouse Employer"));
        contact.setSpousePosition(csvRecord.get("Spouse Position"));
        contact.setInterest(csvRecord.get("Interest"));
        contact.setLeadSource(csvRecord.get("Lead Source"));
        contact.setPriority(csvRecord.get("Priority"));
        contact.setSalesPipelineStage(csvRecord.get("Sales Pipeline Stage"));
        contact.setSecondarySource(csvRecord.get("Secondary Source"));
        contact.setStatus(csvRecord.get("Status"));
        contact.setTimeFrame(csvRecord.get("Time Frame"));
        contact.setPriceRange(csvRecord.get("Price Range"));
        contact.setLocations(csvRecord.get("Locations"));
        contact.setContactGroups(csvRecord.get("Contact Groups"));
        contact.setDeadLeadDate(csvRecord.get("Dead Lead Date"));
        contact.setDeadLeadReason(csvRecord.get("Dead Lead Reason"));
        contact.setCurrentBedrooms(csvRecord.get("Current Bedrooms"));
        contact.setCurrentBaths(csvRecord.get("Current Baths"));
        contact.setCurrentSqFeet(csvRecord.get("Current Sq. Feet"));
        contact.setCurrentGarageSpaces(csvRecord.get("Current Garage Spaces"));
        contact.setCurrentPool(csvRecord.get("Current Pool"));
        contact.setCurrentPropertyType(csvRecord.get("Current Property Type"));
        contact.setCurrentHomeType(csvRecord.get("Current Home Type"));
        contact.setCurrentHomeStyle(csvRecord.get("Current Home Style"));
        contact.setCurrentConstruction(csvRecord.get("Current Construction"));
        contact.setCurrentSpecialRequirements(csvRecord.get("Current Special Requirements"));
        contact.setDesiredBedrooms(csvRecord.get("Desired Bedrooms"));
        contact.setDesiredBaths(csvRecord.get("Desired Baths"));
        contact.setDesiredSqFeet(csvRecord.get("Desired Sq. Feet"));
        contact.setDesiredGarageSpaces(csvRecord.get("Desired Garage Spaces"));
        contact.setDesiredPool(csvRecord.get("Desired Pool"));
        contact.setDesiredPropertyType(csvRecord.get("Desired Property Type"));
        contact.setDesiredHomeType(csvRecord.get("Desired Home Type"));
        contact.setDesiredHomeStyle(csvRecord.get("Desired Home Style"));
        contact.setDesiredConstruction(csvRecord.get("Desired Construction"));
        contact.setViewAmenities(csvRecord.get("View / Amenities"));
        contact.setDesiredSpecialRequirements(csvRecord.get("Desired Special Requirements"));
        contact.setOwnsCurrentResidence(csvRecord.get("Owns Current Residence"));
        contact.setDatePurchasedCurrentHome(csvRecord.get("Date Purchased Current Home"));
        contact.setPrequalified(csvRecord.get("Prequalified"));
        contact.setPrequalifiedAmount(csvRecord.get("Prequalified Amount"));
        contact.setNotes(csvRecord.get("Notes"));
        contact.setCreatedDate(csvRecord.get("Created Date"));
        contact.setModifiedDate(csvRecord.get("Modified Date"));
        return contact;
    }

    private UnimportedContact mapRecordToUnimportedContact(CSVRecord csvRecord, String errorMessage) {
        UnimportedContact unimported = new UnimportedContact();
        unimported.setErrorMessage(errorMessage);

        unimported.setOfficeId(csvRecord.get("Office ID"));
        unimported.setOfficeName(csvRecord.get("Office Name"));
        unimported.setUserId(csvRecord.get("User ID"));
        unimported.setUserName(csvRecord.get("User Name"));
        unimported.setFullName(csvRecord.get("Full Name"));
        unimported.setTitle(csvRecord.get("Title"));
        unimported.setSalutation(csvRecord.get("Salutation"));
        unimported.setEnvelopeLabelSalutation(csvRecord.get("Envelope/Label Salutation"));
        unimported.setCompany(csvRecord.get("Company"));
        unimported.setFirstName(csvRecord.get("First Name"));
        unimported.setMi(csvRecord.get("MI"));
        unimported.setLastName(csvRecord.get("Last Name"));
        unimported.setSpouseFirstName(csvRecord.get("Spouse First Name"));
        unimported.setSpouseMI(csvRecord.get("Spouse MI"));
        unimported.setSpouseLastName(csvRecord.get("Spouse Last Name"));
        unimported.setBirthday(csvRecord.get("Birthday"));
        unimported.setSpouseBirthday(csvRecord.get("Spouse Birthday"));
        unimported.setWeddingAnniversary(csvRecord.get("Wedding Anniversary"));
        unimported.setExpectedMoveDate(csvRecord.get("Expected Move Date"));
        unimported.setTypeOfService(csvRecord.get("Type of Service"));
        unimported.setWebsite(csvRecord.get("Website"));
        unimported.setOfficePhone(csvRecord.get("Office Phone"));
        unimported.setFax(csvRecord.get("Fax"));
        unimported.setInteractionNotes(csvRecord.get("Interaction Notes"));
        unimported.setFullAddress(csvRecord.get("Full Address"));
        unimported.setFullStreetAddress(csvRecord.get("Full Street Address"));
        unimported.setStreetNumber(csvRecord.get("Street Number"));
        unimported.setStreetName(csvRecord.get("Street Name"));
        unimported.setUnit(csvRecord.get("Unit"));
        unimported.setAddress2(csvRecord.get("Address 2"));
        unimported.setAddress3(csvRecord.get("Address 3"));
        unimported.setCity(csvRecord.get("City"));
        unimported.setStateProvince(csvRecord.get("State/Province"));
        unimported.setZipCodePostalCode(csvRecord.get("ZIP Code/Postal Code"));
        unimported.setCountry(csvRecord.get("Country"));
        unimported.setAlternateAddressStreetHouseNumber(csvRecord.get("Alternate Address Street/House Number"));
        unimported.setAlternateAddressStreetName(csvRecord.get("Alternate Address Street Name"));
        unimported.setAlternateAddressUnit(csvRecord.get("Unit")); // Adjust if header differs
        unimported.setAlternateAddressCity(csvRecord.get("Alternate Address City"));
        unimported.setAlternateAddressStateProvinceTerritory(csvRecord.get("Alternate Address State / Province / Territory"));
        unimported.setAlternateAddressZipPostalCode(csvRecord.get("Alternate Address ZIP / Postal Code"));
        unimported.setAlternateAddress2(csvRecord.get("Alternate Address 2"));
        unimported.setAlternateAddress3(csvRecord.get("Alternate Address 3"));
        unimported.setAlternateFullStreetAddress(csvRecord.get("Alternate Full Street Address"));
        unimported.setPreferredPhone(csvRecord.get("Preferred Phone"));
        unimported.setWorkPhone(csvRecord.get("Work Phone"));
        unimported.setHomePhone(csvRecord.get("Home Phone"));
        unimported.setCellPhone(csvRecord.get("Cell Phone"));
        unimported.setSpouseCellPhone(csvRecord.get("Spouse Cell Phone"));
        unimported.setSpouseWorkPhone(csvRecord.get("Spouse Work Phone"));
        unimported.setBestTimeToContact(csvRecord.get("Best Time to Contact"));
        unimported.setFaxAlt(csvRecord.get("Fax"));
        unimported.setEmail(csvRecord.get("Email"));
        unimported.setSecondaryEmail(csvRecord.get("Secondary Email"));
        unimported.setPartnerEmailAddress(csvRecord.get("Partner Email Address"));
        unimported.setDoNotMail(csvRecord.get("Do Not Mail"));
        unimported.setOtherPhone(csvRecord.get("Other Phone"));
        unimported.setPosition(csvRecord.get("Position"));
        unimported.setEmployer(csvRecord.get("Employer"));
        unimported.setSpouseEmployer(csvRecord.get("Spouse Employer"));
        unimported.setSpousePosition(csvRecord.get("Spouse Position"));
        unimported.setInterest(csvRecord.get("Interest"));
        unimported.setLeadSource(csvRecord.get("Lead Source"));
        unimported.setPriority(csvRecord.get("Priority"));
        unimported.setSalesPipelineStage(csvRecord.get("Sales Pipeline Stage"));
        unimported.setSecondarySource(csvRecord.get("Secondary Source"));
        unimported.setStatus(csvRecord.get("Status"));
        unimported.setTimeFrame(csvRecord.get("Time Frame"));
        unimported.setPriceRange(csvRecord.get("Price Range"));
        unimported.setLocations(csvRecord.get("Locations"));
        unimported.setContactGroups(csvRecord.get("Contact Groups"));
        unimported.setDeadLeadDate(csvRecord.get("Dead Lead Date"));
        unimported.setDeadLeadReason(csvRecord.get("Dead Lead Reason"));
        unimported.setCurrentBedrooms(csvRecord.get("Current Bedrooms"));
        unimported.setCurrentBaths(csvRecord.get("Current Baths"));
        unimported.setCurrentSqFeet(csvRecord.get("Current Sq. Feet"));
        unimported.setCurrentGarageSpaces(csvRecord.get("Current Garage Spaces"));
        unimported.setCurrentPool(csvRecord.get("Current Pool"));
        unimported.setCurrentPropertyType(csvRecord.get("Current Property Type"));
        unimported.setCurrentHomeType(csvRecord.get("Current Home Type"));
        unimported.setCurrentHomeStyle(csvRecord.get("Current Home Style"));
        unimported.setCurrentConstruction(csvRecord.get("Current Construction"));
        unimported.setCurrentSpecialRequirements(csvRecord.get("Current Special Requirements"));
        unimported.setDesiredBedrooms(csvRecord.get("Desired Bedrooms"));
        unimported.setDesiredBaths(csvRecord.get("Desired Baths"));
        unimported.setDesiredSqFeet(csvRecord.get("Desired Sq. Feet"));
        unimported.setDesiredGarageSpaces(csvRecord.get("Desired Garage Spaces"));
        unimported.setDesiredPool(csvRecord.get("Desired Pool"));
        unimported.setDesiredPropertyType(csvRecord.get("Desired Property Type"));
        unimported.setDesiredHomeType(csvRecord.get("Desired Home Type"));
        unimported.setDesiredHomeStyle(csvRecord.get("Desired Home Style"));
        unimported.setDesiredConstruction(csvRecord.get("Desired Construction"));
        unimported.setViewAmenities(csvRecord.get("View / Amenities"));
        unimported.setDesiredSpecialRequirements(csvRecord.get("Desired Special Requirements"));
        unimported.setOwnsCurrentResidence(csvRecord.get("Owns Current Residence"));
        unimported.setDatePurchasedCurrentHome(csvRecord.get("Date Purchased Current Home"));
        unimported.setPrequalified(csvRecord.get("Prequalified"));
        unimported.setPrequalifiedAmount(csvRecord.get("Prequalified Amount"));
        unimported.setNotes(csvRecord.get("Notes"));
        unimported.setCreatedDate(csvRecord.get("Created Date"));
        unimported.setModifiedDate(csvRecord.get("Modified Date"));
        return unimported;
    }
}
