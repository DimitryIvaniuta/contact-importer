package com.importservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "unimported_contacts")
public class UnimportedContact {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CI_UNIQUE_ID")
    @SequenceGenerator(name = "CI_UNIQUE_ID", sequenceName = "CI_UNIQUE_ID", allocationSize = 1)
    private Long id;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "office_id")
    private String officeId;

    @Column(name = "office_name")
    private String officeName;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "title")
    private String title;

    @Column(name = "salutation")
    private String salutation;

    @Column(name = "envelope_label_salutation")
    private String envelopeLabelSalutation;

    @Column(name = "company")
    private String company;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "mi")
    private String mi;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "spouse_first_name")
    private String spouseFirstName;

    @Column(name = "spouse_mi")
    private String spouseMI;

    @Column(name = "spouse_last_name")
    private String spouseLastName;

    @Column(name = "birthday")
    private String birthday;

    @Column(name = "spouse_birthday")
    private String spouseBirthday;

    @Column(name = "wedding_anniversary")
    private String weddingAnniversary;

    @Column(name = "expected_move_date")
    private String expectedMoveDate;

    @Column(name = "type_of_service")
    private String typeOfService;

    @Column(name = "website")
    private String website;

    @Column(name = "office_phone")
    private String officePhone;

    @Column(name = "fax")
    private String fax;

    @Column(name = "interaction_notes", columnDefinition = "TEXT")
    private String interactionNotes;

    @Column(name = "full_address", columnDefinition = "TEXT")
    private String fullAddress;

    @Column(name = "full_street_address")
    private String fullStreetAddress;

    @Column(name = "street_number")
    private String streetNumber;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "unit")
    private String unit;

    @Column(name = "address2")
    private String address2;

    @Column(name = "address3")
    private String address3;

    @Column(name = "city")
    private String city;

    @Column(name = "state_province")
    private String stateProvince;

    @Column(name = "zip_code_postal_code")
    private String zipCodePostalCode;

    @Column(name = "country")
    private String country;

    @Column(name = "alternate_address_street_house_number")
    private String alternateAddressStreetHouseNumber;

    @Column(name = "alternate_address_street_name")
    private String alternateAddressStreetName;

    @Column(name = "alternate_address_unit")
    private String alternateAddressUnit;

    @Column(name = "alternate_address_city")
    private String alternateAddressCity;

    @Column(name = "alternate_address_state_province_territory")
    private String alternateAddressStateProvinceTerritory;

    @Column(name = "alternate_address_zip_postal_code")
    private String alternateAddressZipPostalCode;

    @Column(name = "alternate_address2")
    private String alternateAddress2;

    @Column(name = "alternate_address3")
    private String alternateAddress3;

    @Column(name = "alternate_full_street_address")
    private String alternateFullStreetAddress;

    @Column(name = "preferred_phone")
    private String preferredPhone;

    @Column(name = "work_phone")
    private String workPhone;

    @Column(name = "home_phone")
    private String homePhone;

    @Column(name = "cell_phone")
    private String cellPhone;

    @Column(name = "spouse_cell_phone")
    private String spouseCellPhone;

    @Column(name = "spouse_work_phone")
    private String spouseWorkPhone;

    @Column(name = "best_time_to_contact")
    private String bestTimeToContact;

    @Column(name = "fax_alt")
    private String faxAlt;

    @Column(name = "email")
    private String email;

    @Column(name = "secondary_email")
    private String secondaryEmail;

    @Column(name = "partner_email_address")
    private String partnerEmailAddress;

    @Column(name = "do_not_mail")
    private String doNotMail;

    @Column(name = "other_phone")
    private String otherPhone;

    @Column(name = "position")
    private String position;

    @Column(name = "employer")
    private String employer;

    @Column(name = "spouse_employer")
    private String spouseEmployer;

    @Column(name = "spouse_position")
    private String spousePosition;

    @Column(name = "interest")
    private String interest;

    @Column(name = "lead_source")
    private String leadSource;

    @Column(name = "priority")
    private String priority;

    @Column(name = "sales_pipeline_stage")
    private String salesPipelineStage;

    @Column(name = "secondary_source")
    private String secondarySource;

    @Column(name = "status")
    private String status;

    @Column(name = "time_frame")
    private String timeFrame;

    @Column(name = "price_range")
    private String priceRange;

    @Column(name = "locations")
    private String locations;

    @Column(name = "contact_groups")
    private String contactGroups;

    @Column(name = "dead_lead_date")
    private String deadLeadDate;

    @Column(name = "dead_lead_reason", columnDefinition = "TEXT")
    private String deadLeadReason;

    @Column(name = "current_bedrooms")
    private String currentBedrooms;

    @Column(name = "current_baths")
    private String currentBaths;

    @Column(name = "current_sq_feet")
    private String currentSqFeet;

    @Column(name = "current_garage_spaces")
    private String currentGarageSpaces;

    @Column(name = "current_pool")
    private String currentPool;

    @Column(name = "current_property_type")
    private String currentPropertyType;

    @Column(name = "current_home_type")
    private String currentHomeType;

    @Column(name = "current_home_style")
    private String currentHomeStyle;

    @Column(name = "current_construction")
    private String currentConstruction;

    @Column(name = "current_special_requirements", columnDefinition = "TEXT")
    private String currentSpecialRequirements;

    @Column(name = "desired_bedrooms")
    private String desiredBedrooms;

    @Column(name = "desired_baths")
    private String desiredBaths;

    @Column(name = "desired_sq_feet")
    private String desiredSqFeet;

    @Column(name = "desired_garage_spaces")
    private String desiredGarageSpaces;

    @Column(name = "desired_pool")
    private String desiredPool;

    @Column(name = "desired_property_type")
    private String desiredPropertyType;

    @Column(name = "desired_home_type")
    private String desiredHomeType;

    @Column(name = "desired_home_style")
    private String desiredHomeStyle;

    @Column(name = "desired_construction")
    private String desiredConstruction;

    @Column(name = "view_amenities")
    private String viewAmenities;

    @Column(name = "desired_special_requirements", columnDefinition = "TEXT")
    private String desiredSpecialRequirements;

    @Column(name = "owns_current_residence")
    private String ownsCurrentResidence;

    @Column(name = "date_purchased_current_home")
    private String datePurchasedCurrentHome;

    @Column(name = "prequalified")
    private String prequalified;

    @Column(name = "prequalified_amount")
    private String prequalifiedAmount;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_date")
    private String createdDate;

    @Column(name = "modified_date")
    private String modifiedDate;

}
