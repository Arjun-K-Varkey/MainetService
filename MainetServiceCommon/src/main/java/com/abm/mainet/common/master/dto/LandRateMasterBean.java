package com.abm.mainet.common.master.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LandRateMasterBean implements Serializable{


	private static final long serialVersionUID = -12506026553212504L;
	private LandRateMaterDTO rateMaterDTO = new LandRateMaterDTO();
	private List<LandRateMaterDTO> lndRateList = new ArrayList<LandRateMaterDTO>();
	private String btnShow;

	public List<LandRateMaterDTO> getLndRateList() {
		return lndRateList;
	}

	public void setLndRateList(List<LandRateMaterDTO> lndRateList) {
		this.lndRateList = lndRateList;
	}

	public LandRateMaterDTO getRateMaterDTO() {
		return rateMaterDTO;
	}

	public void setRateMaterDTO(LandRateMaterDTO rateMaterDTO) {
		this.rateMaterDTO = rateMaterDTO;
	}


	public String getBtnShow() {
		return btnShow;
	}

	public void setBtnShow(String btnShow) {
		this.btnShow = btnShow;
	}

}
