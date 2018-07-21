package com.kumaev.bookshelf.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Order
 */
@Validated
@Entity
@Table(name = "orders")
public class Order {
  @Id
  @GeneratedValue
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("readerId")
  private Long readerId = null;

  @JsonProperty("bookId")
  private Long bookId = null;

  @JsonProperty("timeOrder")
  private Long timeOrder = null;

  @JsonProperty("complete")
  private Boolean complete = false;

  public Order id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  **/
  @ApiModelProperty(value = "")


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Order readerId(Long readerId) {
    this.readerId = readerId;
    return this;
  }

  /**
   * Get readerId
   * @return readerId
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public Long getReaderId() {
    return readerId;
  }

  public void setReaderId(Long readerId) {
    this.readerId = readerId;
  }

  public Order bookId(Long bookId) {
    this.bookId = bookId;
    return this;
  }

  /**
   * Get bookId
   * @return bookId
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public Long getBookId() {
    return bookId;
  }

  public void setBookId(Long bookId) {
    this.bookId = bookId;
  }

  public Order timeOrder(Long timeOrder) {
    this.timeOrder = timeOrder;
    return this;
  }

  /**
   * Get timeOrder
   * @return timeOrder
  **/
  @ApiModelProperty(value = "")


  public Long getTimeOrder() {
    return timeOrder;
  }

  public void setTimeOrder(Long timeOrder) {
    this.timeOrder = timeOrder;
  }

  public Order complete(Boolean complete) {
    this.complete = complete;
    return this;
  }

  /**
   * Get complete
   * @return complete
  **/
  @ApiModelProperty(value = "")


  public Boolean isComplete() {
    return complete;
  }

  public void setComplete(Boolean complete) {
    this.complete = complete;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Order order = (Order) o;
    return Objects.equals(this.id, order.id) &&
        Objects.equals(this.readerId, order.readerId) &&
        Objects.equals(this.bookId, order.bookId) &&
        Objects.equals(this.timeOrder, order.timeOrder) &&
        Objects.equals(this.complete, order.complete);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, readerId, bookId, timeOrder, complete);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Order {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    readerId: ").append(toIndentedString(readerId)).append("\n");
    sb.append("    bookId: ").append(toIndentedString(bookId)).append("\n");
    sb.append("    timeOrder: ").append(toIndentedString(timeOrder)).append("\n");
    sb.append("    complete: ").append(toIndentedString(complete)).append("\n");
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

