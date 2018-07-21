package com.kumaev.bookshelf.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.*;

/**
 * Statistics
 */
@Validated
@Entity
@Table(name = "statistics")
public class Statistics {
  @Id
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("popularity")
  private Long popularity = 0l;

  @JsonProperty("averageReadingTime")
  private Long averageReadingTime = 0l;

  public Statistics id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Statistics popularity(Long popularity) {
    this.popularity = popularity;
    return this;
  }

  /**
   * Get popularity
   * @return popularity
  **/
  @ApiModelProperty(value = "")


  public Long getPopularity() {
    return popularity;
  }

  public void setPopularity(Long popularity) {
    this.popularity = popularity;
  }

  public Statistics averageReadingTime(Long averageReadingTime) {
    this.averageReadingTime = averageReadingTime;
    return this;
  }

  /**
   * Specified in seconds
   * @return averageReadingTime
  **/
  @ApiModelProperty(value = "Specified in seconds")


  public Long getAverageReadingTime() {
    return averageReadingTime;
  }

  public void setAverageReadingTime(Long averageReadingTime) {
    this.averageReadingTime = averageReadingTime;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Statistics statistics = (Statistics) o;
    return Objects.equals(this.id, statistics.id) &&
        Objects.equals(this.popularity, statistics.popularity) &&
        Objects.equals(this.averageReadingTime, statistics.averageReadingTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, popularity, averageReadingTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Statistics {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    popularity: ").append(toIndentedString(popularity)).append("\n");
    sb.append("    averageReadingTime: ").append(toIndentedString(averageReadingTime)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

