package com.gdu.cashbook.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.Cash;
import com.gdu.cashbook.vo.DayAndPrice;

@Mapper
public interface CashMapper {

	//월별 총액구하기
	public List<DayAndPrice> selectDayAndPriceList(Map<String, Object> map);
	//월별 리스트 띄우기
	public List<Cash> selectCashListByDate(Cash cash);
	
	//수입지출 총액구하기
	public int selectCashFindSum(Cash cash);
	
	//삭제하기
	public void deleteCash(Cash cash);
	
}
