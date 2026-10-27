package in.ameya.controller.master;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import in.ameya.dao.impl.master.IndexController;
import in.ameya.model.master.BranchMasterVo;
import in.ameya.model.master.CountryMasterVo;
import in.ameya.model.master.CurrencyMasterVo;
import in.ameya.model.master.LookupMasterVo;
import in.ameya.model.master.MenuExtVO;
import in.ameya.model.master.MenuVo;
import in.ameya.model.master.NostroAcctsMasterVo;
import in.ameya.util.PrivilegesConstant;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/recon/menu")
public class MenuController {
	
	@Autowired
	IndexController indexController;
	
	@Autowired
	private HttpSession session;
	
	String successPath = "common/error";
	
	
	public boolean checkMenuPrivilege(Model model, String privilegeKey, String action)
	{
		boolean flag = false;
		try
		{			
			@SuppressWarnings("unchecked")
			HashMap<MenuExtVO, String> userPrivilegeList = (HashMap<MenuExtVO, String>) session.getAttribute("userPrivilegeList");
			if(userPrivilegeList != null)
			{
				String privilegeKeyArray[] = privilegeKey.split("#");
				int length = privilegeKeyArray.length;
				for(int i = 0; i < length; i++)
				{				
					Iterator<Entry<MenuExtVO, String>> it = userPrivilegeList.entrySet().iterator();				
					while(it.hasNext())
					{
						MenuExtVO menuExtVO = (MenuExtVO) it.next().getKey();
						if(privilegeKeyArray[i].equalsIgnoreCase(menuExtVO.getPrivilegeId()))
						{
							session.removeAttribute("actionsInfoMenuExtVO");
							session.setAttribute("actionsInfoMenuExtVO", menuExtVO);
							//System.out.println("--->"+menuExtVO.getActionsInfo());
							//String action = null;
							if(action != null)
							{
								if(action.equalsIgnoreCase("AddEdit") && menuExtVO.isActionAddEdit())
								{
									flag = true;				
									break;
								}
								else if(action.equalsIgnoreCase("View") && menuExtVO.isActionView())
								{
									flag = true;				
									break;
								}
								else if(action.equalsIgnoreCase("Authorize") && menuExtVO.isActionAuthorize())
								{
									flag = true;				
									break;
								}
							}else {
								flag = true;				
								break;	
							}
							
							flag = true;				
							break;
						}					
					}
				}	
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if(!flag)
		{
			model.addAttribute("errorMessage", "You are not authorized to access this page(Privilege).");
		}
		return flag;
	}
	
	@PostMapping(value="/dashboard", params={"command=Search"})
	public String dashboard(@ModelAttribute("menuVo") MenuVo menuVo, Model model)
	{
		/*
		 * if(menuController.checkMenuPrivilege(model,
		 * PrivilegesConstant.PRIV_COUNTRY_MASTER)) {
		 */
			successPath = "common/home";
			model.addAttribute("menuVo", menuVo);
			/* } */		
		return successPath;
	}
	
	@PostMapping(value="/masters", params={"command=masters"})
	public String MasterList(@ModelAttribute("menuVo") MenuVo menuVo, Model model)
	{
		/*
		 * if(menuController.checkMenuPrivilege(model,
		 * PrivilegesConstant.PRIV_COUNTRY_MASTER)) {
		 */
			successPath = "common/masters";
			model.addAttribute("menuVo", menuVo);
			/* } */		
		return successPath;
	}
	
	@PostMapping(value="/countryMaster") 
	public String CountryMasterList(@ModelAttribute("menuVo") MenuVo menuVo, Model model)
	{
		if(checkMenuPrivilege(model, PrivilegesConstant.PRIV_COUNTRY_MASTER, null)) 
		{
			CountryMasterVo countryMasterVo = new CountryMasterVo();
			successPath = "masters/countryMasterList";
			model.addAttribute("countryMasterVo",countryMasterVo);
		} 	
		return successPath;
	}
	
	@PostMapping(value="/branchMaster") 
	public String BranchMasterList(@ModelAttribute("menuVo") MenuVo menuVo, Model model)
	{
		if(checkMenuPrivilege(model, PrivilegesConstant.PRIV_BRANCH_MASTER, null))
		{
		 	BranchMasterVo branchMasterVo = new BranchMasterVo();
			successPath = "masters/branchMasterList";
			model.addAttribute("branchMasterVo",branchMasterVo);
		} 	
		return successPath;
	}
	
	@PostMapping(value="/currencyMaster") 
	public String CurrencyMasterList(@ModelAttribute("menuVo") MenuVo menuVo, Model model)
	{
		if(checkMenuPrivilege(model, PrivilegesConstant.PRIV_CURRENCY_MASTER, null))
		{
		 	CurrencyMasterVo currencyMasterVo = new CurrencyMasterVo();
			successPath = "masters/currencyMasterList";
			model.addAttribute("currencyMasterVo",currencyMasterVo);
		} 	
		return successPath;
	}
	
	@PostMapping(value="/nostroAcctsMaster") 
	public String NostroAcctsMasterList(@ModelAttribute("menuVo") MenuVo menuVo, Model model)
	{
		if(checkMenuPrivilege(model, PrivilegesConstant.PRIV_NOSTRO_ACCTS_MASTER, null))
		{
		 	NostroAcctsMasterVo nostroAcctsMasterVo = new NostroAcctsMasterVo();
			successPath = "masters/nostroAcctsMasterList";
			model.addAttribute("nostroAcctsMasterVo",nostroAcctsMasterVo);
		} 	
		return successPath;
	}
	
	@PostMapping(value="/lookupMaster") 
	public String LookupMasterList(@ModelAttribute("menuVo") MenuVo menuVo, Model model)
	{
		if(checkMenuPrivilege(model, PrivilegesConstant.PRIV_LOOKUP_MASTER, null)) 
		{
			LookupMasterVo lookupMasterVo = new LookupMasterVo();
			successPath = "masters/lookupMasterList";
			model.addAttribute("lookupMasterVo",lookupMasterVo);
		} 	
		return successPath;
	}
	
}
