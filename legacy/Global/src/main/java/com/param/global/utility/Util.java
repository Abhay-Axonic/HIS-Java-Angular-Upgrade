package com.param.global.utility;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.param.entity.model.base.UomEntity;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.BatchUomMapper;
import com.param.entity.model.inventory.InwardsUomMapper;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.UnitOfMeasurement;
import com.param.entity.model.master.UomType;
import com.param.global.dto.items.BatchResponse;
import com.param.global.dto.items.UomResponse;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
public class  Util {
	final static Logger logger = Logger.getLogger(Util.class);


	public static Integer getAgeByBirthDate(LocalDate birthDate) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		return Period.between(birthDate, now.toLocalDateTime().toLocalDate()).getYears();
	}

	public static UomResponse getStoreUom(List<UomResponse> uomList) {

		for (UomResponse uom : uomList) {
			if (uom.getStoreUom() != null && uom.getStoreUom()) {
				return uom;
			}
		}
		return null;

	}

	public static UomResponse getOpDispenseUom(List<UomResponse> uomList) {

		for (UomResponse uom : uomList) {
			if (uom.getOpUom() != null && uom.getOpUom()) {
				return uom;
			}
		}

		return null;
	}

	public static int getOpUomConversion(List<UomResponse> uomList) {
		int conversion = 1;
		boolean opUom = false;

		for (UomResponse uom : uomList) {
			if (uom.getOpUom() || opUom) {
				opUom = true;
				conversion = conversion * uom.getConversion();
			}
		}

		return conversion;
	}

	public static int getIpUomConversion(List<UomResponse> uomList) {
		int conversion = 1;
		boolean ipUom = false;

		for (UomResponse uom : uomList) {
			if (uom.getIpUom() || ipUom) {
				ipUom = true;
				conversion = conversion * uom.getConversion();
			}
		}

		return conversion;
	}

	public static int getStoreUomConversion(List<UomResponse> uomList) {
		int conversion = 1;
		boolean storeUom = false;

		for (UomResponse uom : uomList) {
			if (uom.getStoreUom() || storeUom) {
				storeUom = true;
				conversion = conversion * uom.getConversion();
			}
		}

		return conversion;
	}

	public static BatchResponse PopulateBatch(Batch batch) {
		return new BatchResponse(batch.getId(), batch.getBatchNo(), batch.getExpiryDate(), batch.getSellingPrice(),
				batch.getMarkup(), batch.getRate(), PopulateUom(batch), batch.getActualLendingPrice()!=null ? batch.getActualLendingPrice() : 0);
	}

	public static BatchResponse PopulateBatchWithStock(Batch batch) {

		BatchResponse batchResponse= new BatchResponse(batch.getId(), batch.getBatchNo(), batch.getExpiryDate(), batch.getSellingPrice(),
				batch.getStoreStockList().get(0).getAvailableQty(), batch.getMarkup(), batch.getRate(),
				PopulateUom(batch));
		batchResponse.setTaxId(batch.getTax()!=null ? batch.getTax().getId():null);
		batchResponse.setTaxPercentage(batch.getTaxPercentage());
		batchResponse.setActualLendingPrice(batch.getActualLendingPrice());
		return batchResponse;
	}
	public static BatchResponse PopulateBatchWithStock1(Batch batch, Integer storeId) {
       Integer stock=0;
       for(StoreStock s : batch.getStoreStockList())
       {
    	   if(s.getStore().getStoreId().equals(storeId)&& s.getAvailableQty() > 0 && s.getAvailableQty() !=null && s.getTotalQty() > 0)
    	   {
    		   stock=stock+s.getAvailableQty();
    		   
    	   }
    	   
       }
		
		return new BatchResponse(batch.getId(), batch.getBatchNo(), batch.getExpiryDate(), batch.getSellingPrice(),
				stock, batch.getMarkup(), batch.getRate(),
				PopulateUom(batch));

	}
	//added by Minesh for Populate Batch With Stock And Consignment Flag
	public static BatchResponse PopulateBatchWithStockWithConsignmentFlag(Batch batch) {

		return new BatchResponse(batch.getId(), batch.getBatchNo(), batch.getExpiryDate(), batch.getSellingPrice(),
				batch.getStoreStockList().get(0).getAvailableQty(), batch.getMarkup(), batch.getRate(),batch.getIsConsignment(),
				PopulateUom(batch));
	}
	//end code  by minesh for Populate Batch With Stock And Consignment Flag

	public static BatchResponse PopulateBatchWithStockWithFlag(Batch batch, Boolean isSelected) {
		BatchResponse bres=new BatchResponse(batch.getId(), batch.getBatchNo(), batch.getExpiryDate(), batch.getSellingPrice(),
				batch.getStoreStockList().get(0).getAvailableQty(), batch.getMarkup(), batch.getRate(),
				PopulateUom(batch), isSelected);
		bres.setTaxPercentage(batch.getTaxPercentage()!=null ? batch.getTaxPercentage() : 0);
		bres.setTaxId(batch.getTax()!=null ? batch.getTax().getId():0);
		bres.setTaxAmount(batch.getTaxAmount()!=null ? batch.getTaxAmount() :0);
		bres.setActualLendingPrice(batch.getActualLendingPrice()!=null ? batch.getActualLendingPrice() :0);
		bres.setMrp(batch.getRate());
		return bres;
	}

	/*
	 * public static List<UomResponse> PopulateUom(Batch batch) { List<UomResponse>
	 * response = new ArrayList<UomResponse>();
	 * 
	 * for (BatchUomMapper uomMapper : batch.getBatchUomList()) { response.add(new
	 * UomResponse(uomMapper.getUomType().getId(), uomMapper.getUomType().getType(),
	 * uomMapper.getUomUnit().getId(), uomMapper.getUomUnit().getUnits(),
	 * uomMapper.getUomType().getCode() + " - " + uomMapper.getUomUnit().getUnits(),
	 * uomMapper.getConversions(), uomMapper.getIpUom(), uomMapper.getOpUom(),
	 * uomMapper.getStoreUom())); }
	 * 
	 * return (List<UomResponse>) uomResponse; }
	 */
	public static List<UomResponse> PopulateUom(Batch batch) {
		List<UomResponse> response = new ArrayList<UomResponse>();

		for (BatchUomMapper uomMapper : batch.getBatchUomList()) {
			if(uomMapper!=null)
			{
					UomResponse uom = new UomResponse(
					uomMapper.getUomType().getId(),
					uomMapper.getUomType().getType(),
					uomMapper.getUomUnit().getId(), 
					uomMapper.getUomUnit().getUnits(),
					uomMapper.getUomType().getCode() + " - " + uomMapper.getUomUnit().getUnits(),
					uomMapper.getConversions(),
					uomMapper.getIpUom(), uomMapper.getOpUom(), uomMapper.getStoreUom());
			response.add(uom);
			}
		}
		Collections.sort(response, new CustomComparator());

		return response;
	}
	
		public static List<UomResponse> PopulateUom(List<UnitOfMeasurement> unitOfMeasurementList) {
		List<UomResponse> response = new ArrayList<UomResponse>();

		for (UnitOfMeasurement uom : unitOfMeasurementList) {
			response.add(new UomResponse(uom.getUomType().getId(), uom.getUomType().getType(), uom.getUomUnit().getId(),
					uom.getUomUnit().getUnits(), uom.getUomType().getCode() + " - " + uom.getUomUnit().getUnits(),
					uom.getConversions(), uom.getIpUom(), uom.getOpUom(), uom.getStoreUom()));
		}
		Collections.sort(response, new CustomComparator());
		return response;
	}

	public static Date getDate(Date date) {

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date dateFinal = new SimpleDateFormat("yyyy-MM-dd").parse(dateFormat.format(date));
			return dateFinal;
		} catch (Exception ex) {
		logger.error("getDate failed", ex);
			return date;
		}
	}

	public static <T extends InwardsUomMapper> Integer GetLeaseApprovedQuantity(Integer quantity,
			List<InwardsUomMapper> uomList, UomType uomType) {
		int conversion = 1;
		boolean calculate = false;

		uomList.sort(new Comparator<InwardsUomMapper>() {
			@Override
			public int compare(InwardsUomMapper uom1, InwardsUomMapper uom2) {
				if (uom1.getUomType().getId().equals(uom2.getUomType().getId())) {
					return 0;
				}

				return uom1.getUomType().getId() < uom2.getUomType().getId() ? -1 : 1;
			}
		});

		for (InwardsUomMapper uom : uomList) {
			if (uom.getUomType().getId().equals(uomType.getId()) || calculate) {
				calculate = true;
				conversion = conversion * uom.getConversion();
			}
		}
		return quantity * conversion;
	}

	public static <T extends InwardsUomMapper> Double GetMrpLeaseQuantityForMatInw(Double mrp,
			List<InwardsUomMapper> uomList, UomType uomType) {
		int conversion = 1;
		boolean calculate = false;

		uomList.sort(new Comparator<InwardsUomMapper>() {
			@Override
			public int compare(InwardsUomMapper uom1, InwardsUomMapper uom2) {
				if (uom1.getUomType().getId().equals(uom2.getUomType().getId())) {
					return 0;
				}

				return uom1.getUomType().getId() < uom2.getUomType().getId() ? -1 : 1;
			}
		});

		for (InwardsUomMapper uom : uomList) {
			if (uom.getUomType().getId().equals(uomType.getId()) || calculate) {
				calculate = true;
				conversion = conversion * uom.getConversion();
			}
		}

		return mrp * conversion;
	}

	public static <T extends UomEntity> Integer GetLeaseQuantity(Integer quantity, List<T> uomList, UomType uomType) {
		int conversion = 1;
		boolean calculate = false;

		uomList.sort(new Comparator<T>() {
			@Override
			public int compare(T uom1, T uom2) {
				if (uom1.getUomType().getId().equals(uom2.getUomType().getId())) {
					return 0;
				}

				return uom1.getUomType().getId() < uom2.getUomType().getId() ? -1 : 1;
			}
		});

		for (T uom : uomList) {
			if (uom.getUomType().getId().equals(uomType.getId()) || calculate) {
				calculate = true;
				conversion = conversion * uom.getConversions();
			}
		}
		return quantity * conversion;
	}
	
	public static <T extends UomEntity> Integer GetLeaseQuantityPurchase(Integer quantity, List<T> uomList, UomType uomType) {
		int conversion = 1;
		boolean calculate = false;

		uomList.sort(new Comparator<T>() {
			@Override
			public int compare(T uom1, T uom2) {
				if (uom1.getUomType().getId().equals(uom2.getUomType().getId())) {
					return 0;
				}

				return uom1.getUomType().getId() < uom2.getUomType().getId() ? -1 : 1;
			}
		});

		for (T uom : uomList) {
			if (uom.getUomType().getId().equals(uomType.getId()) || calculate) {
				calculate = true;
				conversion = conversion * uom.getConversions();
			}
		}
		return quantity / conversion;
	}
	
	public static <T extends UomEntity> Integer GetLeaseQuantity(Integer quantity, List<T> uomList, Integer uomType) {
		int conversion = 1;
		boolean calculate = false;

		uomList.sort(new Comparator<T>() {
			@Override
			public int compare(T uom1, T uom2) {
				if (uom1.getUomType().getId().equals(uom2.getUomType().getId())) {
					return 0;
				}

				return uom1.getUomType().getId() < uom2.getUomType().getId() ? -1 : 1;
			}
		});

		for (T uom : uomList) {
			if (uom.getUomType().getId().equals(uomType) || calculate) {
				calculate = true;
				conversion = conversion * uom.getConversions();
			}
		}
		return quantity * conversion;
	}
	
	public static Integer GetPendingIndentQuantityLeaseQuantity(Long quantity, List<UnitOfMeasurement> uomList, Integer uomType) {
		int conversion = 1;
		boolean calculate = false;

		uomList.sort(new Comparator<UnitOfMeasurement>() {
			@Override
			public int compare(UnitOfMeasurement uom1, UnitOfMeasurement uom2) {
				if (uom1.getId().equals(uom2.getUomType().getId())) {
					return 0;
				}

				return uom1.getUomType().getId() < uom2.getUomType().getId() ? -1 : 1;
			}
		});

		for (UnitOfMeasurement uom : uomList) {
			if (uom.getUomType().getId().equals(uomType) || calculate) {
				calculate = true;
				conversion = conversion * uom.getConversions();
			}
		}
		return (int) (quantity * conversion);
	}
	
	public static Integer GetPendingIndentQuantityLeaseQuantityStoreIndent(Integer quantity, List<UnitOfMeasurement> uomList, Integer uomType) {
		int conversion = 1;
		boolean calculate = false;

		uomList.sort(new Comparator<UnitOfMeasurement>() {
			@Override
			public int compare(UnitOfMeasurement uom1, UnitOfMeasurement uom2) {
				if (uom1.getId().equals(uom2.getUomType().getId())) {
					return 0;
				}

				return uom1.getUomType().getId() < uom2.getUomType().getId() ? -1 : 1;
			}
		});

		for (UnitOfMeasurement uom : uomList) {
			if (uom.getUomType().getId().equals(uomType) || calculate) {
				calculate = true;
				conversion = conversion * uom.getConversions();
			}
		}
		return (int) (quantity * conversion);
	}
	
	public static Integer GetPendingIndentQuantityLeaseQuantity( List<UnitOfMeasurement> uomList, Integer uomType) {
		int conversion = 1;
		boolean calculate = false;

		uomList.sort(new Comparator<UnitOfMeasurement>() {
			@Override
			public int compare(UnitOfMeasurement uom1, UnitOfMeasurement uom2) {
				if (uom1.getId().equals(uom2.getUomType().getId())) {
					return 0;
				}

				return uom1.getUomType().getId() < uom2.getUomType().getId() ? -1 : 1;
			}
		});

		for (UnitOfMeasurement uom : uomList) {
			if (uom.getUomType().getId().equals(uomType) || calculate) {
				calculate = true;
				conversion = conversion * uom.getConversions();
			}
		}
		return (int) (conversion);
	}
	
	public static <T extends UnitOfMeasurement> Integer ConvertQuantityByUomForLocalPurReq(Integer quantity,
			List<UnitOfMeasurement> uomList, UomType uomType) {
		int conversion = 1;
		boolean calculate = false;

		uomList.sort(new Comparator<UnitOfMeasurement>() {
			@Override
			public int compare(UnitOfMeasurement uom1, UnitOfMeasurement uom2) {
				if (uom1.getUomType().getId().equals(uom2.getUomType().getId())) {
					return 0;
				}

				return uom1.getUomType().getId() < uom2.getUomType().getId() ? -1 : 1;
			}
		});

		for (UnitOfMeasurement uom : uomList) {
			if (uom.getUomType().getId().equals(uomType.getId()) || calculate) {
				calculate = true;
				conversion = conversion * uom.getConversions();
			}
		}
		return quantity * conversion;
	}

	public static <T extends UomEntity> Double GetMrpLeaseQuantity(Double mrp, List<T> uomList, UomType uomType) {
		int conversion = 1;
		boolean calculate = false;

		uomList.sort((uom1, uom2) -> {
			if (uom1.getUomType().getId().equals(uom2.getUomType().getId())) {
				return 0;
			}

			return uom1.getUomType().getId() < uom2.getUomType().getId() ? -1 : 1;
		});

		for (T uom : uomList) {
			if (uom.getUomType().getId().equals(uomType.getId()) || calculate) {
				calculate = true;
				conversion = conversion * uom.getConversions();
			}
		}

		return mrp / conversion;
	}

	public static <T extends UomEntity> Long GetMaxStoreConvertedQty(Long maxStoreQuantity, List<T> uomList,
			UomType uomType) {
		int conversion = 1;
		boolean calculate = false;

		uomList.sort(new Comparator<T>() {
			@Override
			public int compare(T uom1, T uom2) {
				if (uom1.getUomType().getId().equals(uom2.getUomType().getId())) {
					return 0;
				}

				return uom1.getUomType().getId() < uom2.getUomType().getId() ? -1 : 1;
			}
		});

		for (T uom : uomList) {
			if (uom.getUomType().getId().equals(uomType.getId()) || calculate) {
				calculate = true;
				conversion = conversion * uom.getConversions();
			}
		}
		return maxStoreQuantity / conversion;
	}

	public static <T extends UnitOfMeasurement> Long ConvertQuantityByUom(Long maxStoreQuantity,
			List<UnitOfMeasurement> uomList, UomType uomType) {
		int conversion = 1;
		boolean calculate = false;

		uomList.sort(new Comparator<UnitOfMeasurement>() {
			@Override
			public int compare(UnitOfMeasurement uom1, UnitOfMeasurement uom2) {
				if (uom1.getUomType().getId().equals(uom2.getUomType().getId())) {
					return 0;
				}

				return uom1.getUomType().getId() < uom2.getUomType().getId() ? -1 : 1;
			}
		});

		for (UnitOfMeasurement uom : uomList) {
			if (uom.getUomType().getId().equals(uomType.getId()) || calculate) {
				calculate = true;
				conversion = conversion * uom.getConversions();
			}
		}
		return (maxStoreQuantity < 0 ? 0 :  maxStoreQuantity / conversion);
	}

	public static <T extends UomEntity> Long ConvertQuantity(Long maxStoreQuantity, List<T> uomList, UomType uomType) {
		int conversion = 1;
		boolean calculate = false;

		uomList.sort(new Comparator<T>() {
			@Override
			public int compare(T uom1, T uom2) {
				if (uom1.getUomType().getId().equals(uom2.getUomType().getId())) {
					return 0;
				}

				return uom1.getUomType().getId() < uom2.getUomType().getId() ? -1 : 1;
			}
		});

		for (T uom : uomList) {
			if (uom.getUomType().getId().equals(uomType.getId()) || calculate) {
				calculate = true;
				conversion = conversion * uom.getConversions();
			}
		}
		return maxStoreQuantity / conversion;
	}

	public static UomResponse getIpUom(List<UomResponse> uomList) {
		for (UomResponse uom : uomList) {
			if (uom.getIpUom() != null && uom.getIpUom()) {
				return uom;
			}
		}
		return null;
	}
	public static <T extends UomEntity> Integer GetActualIssuedQuantity(Integer quantity, List<T> uomList, UomType uomType) {
		Double conversion = 1.0;
		boolean calculate = false;

		uomList.sort(new Comparator<T>() {
			@Override
			public int compare(T uom1, T uom2) {
				if (uom1.getUomType().getId().equals(uom2.getUomType().getId())) {
					return 0;
				}

				return uom1.getUomType().getId() < uom2.getUomType().getId() ? -1 : 1;
			}
		});

		for (T uom : uomList) {
			if (uom.getUomType().getId().equals(uomType.getId()) || calculate) {
				calculate = true;
				conversion = conversion * uom.getConversions();
			}
		}
		Double convertedQty=(quantity/conversion);
		return (int) Math.ceil(convertedQty);
	}

	public static BatchResponse PopulateBatchForKit(Batch batch) {
		return new BatchResponse(batch.getId(), batch.getBatchNo(), batch.getExpiryDate(), batch.getSellingPrice(),
				batch.getMarkup(), batch.getRate(), batch.getActualLendingPrice()!=null ? batch.getActualLendingPrice() : 0);
	}

}

class CustomComparator implements Comparator<UomResponse> {
	@Override
	public int compare(UomResponse u1, UomResponse u2) {
		return u1.getUomTypeId().compareTo(u2.getUomTypeId());
	}
}
