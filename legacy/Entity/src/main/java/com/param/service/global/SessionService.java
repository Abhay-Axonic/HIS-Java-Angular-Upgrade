package com.param.service.global;

import org.springframework.stereotype.Service;

import com.param.entity.model.base.BaseEntity;
import com.param.service.global.model.CookieObjectAppConfig;
import com.param.service.global.model.CookieObjectDto;

@Service("SessionService")
//@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionService {

    private CookieObjectDto cookieObjectDto;
    
    private CookieObjectAppConfig cookieObjectAppConfig;
    

    public CookieObjectDto getCookieObjectDto() {
        return this.cookieObjectDto;
    }

    public void setCookieObjectDto(CookieObjectDto cookieObjectDto) {
        this.cookieObjectDto = cookieObjectDto;
    }
    
    

    public CookieObjectAppConfig getCookieObjectAppConfig() {
		return cookieObjectAppConfig;
	}

	public void setCookieObjectAppConfig(CookieObjectAppConfig cookieObjectAppConfig) {
		this.cookieObjectAppConfig = cookieObjectAppConfig;
	}

	public Integer getOrganizationId() {
        if (this.cookieObjectDto != null) {
            return this.cookieObjectDto.getOrganizationId() != null ? this.cookieObjectDto.getOrganizationId() : null;
        }

        return null;
    }

    public Integer getUnitId() {
        if (this.cookieObjectDto != null) {
            return this.cookieObjectDto.getUnitId() != null ? this.cookieObjectDto.getUnitId() : null;
        }

        return null;
    }

	public Integer getCounterId() {
		 if (this.cookieObjectDto != null) {
	            return this.cookieObjectDto.getCounterId() != null ? this.cookieObjectDto.getCounterId() : null;
	        }
		 return null;
	}

    
    public Integer getUserId() {
        if (this.cookieObjectDto != null) {
            return this.cookieObjectDto.getUserId() != null ? this.cookieObjectDto.getUserId() : null;
        }

        return null;
    }

    public Integer getStoreId() {
        if (this.cookieObjectDto != null) {
            return this.cookieObjectDto.getStoreId() != null ? this.cookieObjectDto.getStoreId() : null;
        }

        return null;
    }
    
	public Integer getSelfPayeeId() {
        if (this.cookieObjectAppConfig != null) {
            return this.cookieObjectAppConfig.getSelfPayeeId() != null ? this.cookieObjectAppConfig.getSelfPayeeId() : null;
        }

        return null;
    }
	
	//Billing
	
	//Smart Card
	public Integer getDefaultSelfTariffId() {
        if (this.cookieObjectAppConfig != null) {
            return this.cookieObjectAppConfig.getDefaultSelfTariffId() != null ? this.cookieObjectAppConfig.getDefaultSelfTariffId() : null;
        }
        return null;
    }
	
	//Smart Card
	public String getGroupPracticeNo() {
        if (this.cookieObjectAppConfig != null) {
            return this.cookieObjectAppConfig.getGroupPracticeNo() != null ? this.cookieObjectAppConfig.getGroupPracticeNo() : null;
        }

        return null;
    }
	
	public String getGroupPracticeName() {
        if (this.cookieObjectAppConfig != null) {
            return this.cookieObjectAppConfig.getGroupPracticeName() != null ? this.cookieObjectAppConfig.getGroupPracticeName() : null;
        }

        return null;
    }
	
	public String getStartDateFormat() {
        if (this.cookieObjectAppConfig != null) {
            return this.cookieObjectAppConfig.getStartDateFormat() != null ? this.cookieObjectAppConfig.getStartDateFormat() : null;
        }

        return null;
    }
	
	public String getPracticeNo() {
        if (this.cookieObjectAppConfig != null) {
            return this.cookieObjectAppConfig.getPracticeNo() != null ? this.cookieObjectAppConfig.getPracticeNo() : null;
        }

        return null;
    }
	
	public Character getIsPreOrPostBilling() {
		 if (this.cookieObjectAppConfig != null) {
	            return this.cookieObjectAppConfig.getIsPreOrPostBilling() != null ? this.cookieObjectAppConfig.getIsPreOrPostBilling() : null;
	        }
		 return null;
	}

    public void setStoreId(int id) {
        if (this.cookieObjectDto != null) {
            this.cookieObjectDto.setStoreId(id);
        }
    }

    /*public void setEntityDefaults(BaseEntity entity) {
        if (this.cookieObjectDto != null) {
            //entity.setDefaults();
        }
    }*/

    /*public void setEntityUpdateDefaults(BaseEntity entity) {
        if (this.cookieObjectDto != null) {
            entity.setUpdateDefaults();
        }
    }*/


}
