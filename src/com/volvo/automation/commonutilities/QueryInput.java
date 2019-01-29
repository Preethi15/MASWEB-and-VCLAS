package com.volvo.automation.commonutilities;

public interface QueryInput {

	/*
	 * Query: Tugger Flow Query Updated: Renuka devi Description: Tugger Queries -
	 * Pre-requistic queries to clear MASWEB and VCLAS application
	 */

	// MASWEB
	// Tugger Query to Update Status in Masweb table for Line Movement
	public String tugger_MASWEB_UpdateStatus = "Update MAS.Pmr473 set STATUS = '99' where ARTNR = '#FLOW#' and STATUS = '30'";
	// Delete query to clear data of tugger flow
	public String tugger_MASWEB_DeleteFlow = "delete from MAS.Pmr758 where SEKVTYP = '#FLOW#'";

	// VCLAS
	public String tugger_VCLAS_SelectTransport = "Select * from transport_order where  object ='#FLOW#'";

	// delete query to remove Assignment_ID in Alarm table(1)
	public String tugger_VCLAS_Alarm = "Delete from alarm where ASSIGNMENT_ID  in (select ID from TRANSPORT_ASSIGNMENT where TRANSPORT_ORDER in (select ID from transport_order where  object ='#FLOW#'))";

	// delete query to remove TA_ID in TA_PARAM table(2)
	public String tugger_VCLAS_TA_Param = "Delete from TA_PARAM where TA_ID in (select ID from TRANSPORT_ASSIGNMENT where TRANSPORT_ORDER in (select ID from transport_order where  object ='#FLOW#'))";

	// delete query to remove actor_id in planning_trigger table(3)
	public String tugger_VCLAS_planing_trigger = "Delete from planning_trigger where Actor_id in (select ID from TRANSPORT_ASSIGNMENT where TRANSPORT_ORDER in (select ID from transport_order where  object ='#FLOW#'))";

	// delete query to remove transport_order in TRANSPORT_ASSIGNMENT table
	public String tugger_VCLAS_Transport_Assignment = "Delete from TRANSPORT_ASSIGNMENT where TRANSPORT_ORDER in (select ID from transport_order where object ='#FLOW#')";

	// delete query to remove TRANSPORT_ORDER_ID in MAS_PACKAGE table(5)
	public String tugger_VCLAS_MAS_Package = "Delete from MAS_PACKAGE where TRANSPORT_ORDER_ID in (select ID from transport_order where  object ='#FLOW#')";

	// delete query to remove TO_ID in TO_PARAM table(6)
	public String tugger_VCLAS_TO_PARAM = "Delete from TO_PARAM where TO_ID in (select ID from transport_order where  object ='#FLOW#')";

	// delete query to remove flow in transport order table(7)
	public String tugger_VCLAS_transport_order = "Delete from transport_order where  object ='#FLOW#'";

	/* ---------- End of Tugger Queries --------------------------- */

	/*
	 * Query: V90 Querty Updated: Keerthana Description: V90 Queries - Pre-requistic
	 * queries to clear V90 application
	 */

	// MASWEB
	public String V90_MASWEB_SelectRFID = "Select * from MAS.PMR840 where RFID='#FLOW#'";

	// Delete query to clear data of V90 flow
	public String V90_MASWEB_DeleteRFID = "Delete from MAS.PMR840 where RFID='#FLOW#'";

	// MASWEB
	public String V90_MASWEB_SelectBodyId = "Select * from MAS.PMR883 where BODYID='#FLOW#'";

	// Delete query to clear data of V90 flow
	public String V90_MASWEB_DeleteBodyId = "Delete from MAS.PMR883 where BODYID='#FLOW#'";

	/* ---------- End of V90 Queries --------------------------- */

	/*
	 * Query: JISTJIT Tugger Querty 
	 * Updated: Keerthana 
	 * Description: Tugger Queries -Pre-requistic queries to clear MASWEB and VCLAS application
	 */

	//MASWEB 
	
	// To fetch the records of Jisjit flow
	public String JISJIT_MASWEB_FBOPNR_LBOPNR = "select * from MAS.PMR861 where PARTFAM='#FLOW#'";

	// To fetch the BEOLOPNR with FBOPNR and LBOPNR
	public String JISJIT_MASWEB_BEOLOPNR = "select p885.BEOLOPNR from MAS.Pmr852 p852 ,MAS.PMR885 p885 where (p852.BOPLOPNR >= '#FBOPNR#' AND p852.BOPLOPNR <= '#LBOPNR#')"
			+ " and p852.FYON = p885.FYON and p852.BOPLOPNR=p885.BOPLOPNR and trim(p852.ASNNR) = '#FSNR#' and p885.DRPFLG='N' "
			+ "order by p885.BEOLOPNR desc";

	// To set the Kamera Id with IDLOPNR
	public String JISJIT_MASWEB_KameraId = "Update MAS.PMR647 set IDLOPNR=#BEOLOPNR# where KAMERAID='#KAMERAID#'";

	// To fetch the count of records with particular FSNR
	public String JISJIT_MASWEB_FSNR_COUNT = "select count(*) from MAS.PMR861 where PARTFAM='#FLOW#' and TRIM(FSNR) ='#FSNR#'";

	// To fetch all the records with particular FSNR
	public String JISJIT_MASWEB_FSNR = "select * from MAS.PMR861 where PARTFAM='#FLOW#' and TRIM(FSNR) ='#FSNR#'";

	public String JISJIT_MASWEB_DummyFsnr = "select * from MAS.PMR861 where PARTFAM='#FLOW#'";

	// To delete the paricular record with rack no.
	public String JISJIT_MASWEB_DeleteRecord = "delete from MAS.PMR861 where RACKNR = '#RACKNR#'";

	// To insert the particular record
	public String JISJIT_MASWEB_InsertRecord = "insert into MAS.PMR861 (PARTFAM,LEVNR,OKOLLINR,RACKNR,CONTNR,FSNR,FBOPNR,LBOPNR) values('#FLOW#','#LEVNR#','#OKOLLINR#','#deletedRackNo#','#CONTNR#','#FSNR#','#Dfbopnr#','#Dlbopnr#')";

	// To fetch the last record in that particular FSNR
	public String JISJIT_MASWEB_LASTRACK = "select max(RACKNR) from MAS.PMR861 where PARTFAM='#FLOW#' and TRIM(FSNR) ='#FSNR#'";

	// To fetch the first record in that particular FSNR
	public String JISJIT_MASWEB_RACKSTART = "select * from MAS.PMR861 where PARTFAM='#FLOW#' and TRIM(FSNR) ='#FSNR#' and RACKNR between '#RACKSTART#' and '#RACKEND#'";

	// To fetch the particular record using Rack no.
	public String JISJIT_MASWEB_SelectRecord_DeletedRackNo = "select * from MAS.PMR861 where RACKNR = '#RACKNR#'";

	//VCLAS 
	
	// To fetch the article count for Jisjit flow in VCLAS DB
	public String VCLAS_ArticleCount = "select * from ARTICLE_COUNT where NAME='#FLOW#'";

	/* ---------- End of JISJIT Tugger Queries --------------------------- */

	/*
	 * Query:Emulator order no fetching Querty Updated: Preethi Madheshwaran
	 * Description: AGV_emulator_Queries - Pre-requistic queries to clear MASWEB and
	 * VCLAS application
	 */

	// To Fetch Id from Transport Order table W.R.T task id

	public String AGV_Emulator_TransportOrder = "select id from TRANSPORT_ORDER where task_ID=#TASKID#";

	// To fetch the order no for second full task of AGV

	public String AGV_Emulator_Transport_Assignment_FullTask = "select * from TRANSPORT_ASSIGNMENT where TRANSPORT_ORDER=#ID# and PARENT_ID!=0  and status='STATUS_WAITING'";

	// To fetch the orde no for First empty task for AGV

	public String AGV_Emulator_Transport_Assignemnt_EmptyTask = "select * from TRANSPORT_ASSIGNMENT where TRANSPORT_ORDER=#ID# and PARENT_ID!=0 and status='STATUS_LEDIG'";

// Masweb Queries
    
    public String AGV_ReserveTaskID_PMR473="Select * from MAS.PMR473 where TASKID in(#EmptyTaskID#,#FullTaskTD#)";
    
    public String AGV_ReserveTaskID_PMR471="Select * from MAS.PMR471 where TASKID in(#EmptyTaskID#,#FullTaskTD#)";
    
    public String AGV_ReserveTaskID_PMR476="select * from MAS.PMR476 where TASKID in (#EmptyTaskID#,#FullTaskTD#)";
    
    public String AGV_ReserveTaskID_PMR478="Select * from MAS.PMR478 where TASKID in(#EmptyTaskID#,#FullTaskID#)";
    
   // Vclas Queries
    
    public String AGV_ReserveTaskID_TRANSPORT_ORDER ="Select id from TRANSPORT_ORDER where TASK_ID IN (#EmptyTaskID#,#FullTaskTD#)";
    
    public String AGV_ReserveTaskID_Transport_Assignment="select * from TRANSPORT_ASSIGNMENT where TRANSPORT_ORDER in (#FOrderId#,#EOrderId#)";
    
   public String AGV_ReserveTaskID_AGV_Order_status ="Select * from AGV_ORDER_STATUS where ORDER_NUMBER in (select ID from TRANSPORT_ASSIGNMENT where TYPE='AGVERACK' and STATUS='STATUS_LEDIG')" ;
    
    // TWO QUERIIES NEED TO BE ADDED

   /*
	 * Query: LDJIT Trailer Querty 
	 * Updated: Keerthana 
	 * Description: Trailer Queries -Pre-requistic queries to clear MASWEB and VCLAS application
	 */

	//MASWEB 
	
	// To fetch the records of pmr 754 ldjit flow for getting CheckCode
	public String LDJIT_MASWEB_CHECKCODE = "select * from MAS.PMR754 where AREA='#parkingPlace#'";
	
	// To fetch the records of pmr 862 ldjit flow for getting record count 
		public String LDJIT_MASWEB_AFTERGR_COUNT = "select count(*) from pmr862 where contnr='#CONTNR#' and POSITION='#POSITION#' AND FLOW='#FLOW#'"; 
		
		// To fetch the records of pmr 861 ldjit flow for getting record count 
				public String LDJIT_MASWEB_AFTERGR_COUNT_FSNR = "select count(*) from MAS.PMR861 where PARTFAM='#FLOW#' and TRIM(FSNR)='#FSNR#'";

}
