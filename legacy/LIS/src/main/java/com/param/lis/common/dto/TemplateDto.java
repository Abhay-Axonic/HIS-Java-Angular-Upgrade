package com.param.lis.common.dto;

public class TemplateDto {

  
  Integer labTemplateId;   
  String title;
  String image;
  String description;
  String html;
  Integer templateId;   
  Integer templateFormatId;   
  
  public Integer getLabTemplateId() {
    return labTemplateId;
  }
  public void setLabTemplateId(Integer labTemplateId) {
    this.labTemplateId = labTemplateId;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public String getImage() {
    return image;
  }
  public void setImage(String image) {
    this.image = image;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getHtml() {
    return html;
  }
  public void setHtml(String html) {
    this.html = html;
  }
public Integer getTemplateId() {
	return templateId;
}
public void setTemplateId(Integer templateId) {
	this.templateId = templateId;
}
public Integer getTemplateFormatId() {
	return templateFormatId;
}
public void setTemplateFormatId(Integer templateFormatId) {
	this.templateFormatId = templateFormatId;
}
  
  
}
