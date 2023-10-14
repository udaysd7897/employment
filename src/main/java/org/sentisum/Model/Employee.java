package org.sentisum.Model;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "work")
public class Employee {

    @Id
    private String id;

    @JsonProperty("title")
    @Field(type = FieldType.Text)
    private String title;

    @JsonProperty("employer")
    @Field(type = FieldType.Text)
    private String employer;

    @JsonProperty("location")
    @Field(type = FieldType.Text)
    private String location;

    @JsonProperty("gender")
    @Field(type = FieldType.Text)
    private String gender;

    @JsonProperty("salary")
    @Field(type = FieldType.Float)
    private float salary;

    @JsonProperty("timestamp")
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "MM/dd/yyyy HH:mm:ss")
    private String timestamp;


    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }


    public float getSalary() {
        return salary;
    }

}