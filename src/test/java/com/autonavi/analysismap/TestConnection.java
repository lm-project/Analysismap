package com.autonavi.analysismap;


import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:service-context.xml")
public class TestConnection {

	/*@Autowired
	private UserService userService;*/
	
	/*@Test
	public void getRolesTest(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pageIndex", 1); 
		map.put("recordCountPerPage", 9); 
		map.put("role", 0);
		map.put("truename", "");
		map = userService.getUserList(map);
		List<User> list = (List<User>) map.get("users");
		System.out.println(list.get(0).getTruename());
		Assert.assertEquals(5, list.size());
	}*/
	

}
