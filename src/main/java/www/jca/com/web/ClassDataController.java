package www.jca.com.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import www.jca.com.service.MenuService;
import www.jca.com.vo.Board;
import www.jca.com.vo.FileInfo;
import www.jca.com.vo.Menus;
import www.jca.com.vo.Paging;

@Controller
public class ClassDataController extends JCAController implements BoardController<Board> {
	@Autowired
	MenuService menuService;
	
	@RequestMapping("/class/data/list")
	@Override
	public ModelAndView getListView(ModelAndView mv, Board model) {
		List<Board> boardList = new ArrayList<Board>();
		
		RestTemplate rest = new RestTemplate();
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append("http://jcoding.kr/api/board/list/").append(model.getBoardType());
		if(model.getPageNo() > 0) {
			urlBuilder.append("/").append(model.getPageNo());
		}
		if (model.getQuery()!=null && model.getQuery().length() > 0 ) {
			urlBuilder.append("?query=").append(model.getQuery());
		}
		logger.info(""+urlBuilder.toString());
		JSONObject json = new JSONObject(rest.getForObject(urlBuilder.toString(),  String.class));
		try {
			JSONArray array = json.getJSONArray("list");
			for(int i=0; i<array.length(); i++) {
				boardList.add(Board.parseBoard(array.getJSONObject(i)));
			}
		} catch(JSONException e) {
			e.printStackTrace();
		}
		
		StringBuilder urlBuilder2 = new StringBuilder();
		urlBuilder2.append("http://jcoding.kr/api/board/count/").append(model.getBoardType());
		if(model.getPageNo() > 0) {
			urlBuilder2.append("/").append(model.getPageNo());
		}
		if (model.getQuery()!=null && model.getQuery().length() > 0 ) {
			urlBuilder2.append("?query=").append(model.getQuery());
		}
		Board paging = rest.getForObject(urlBuilder2.toString(), Board.class);
				logger.info(paging.toString());
				mv.addObject("paging", paging);
		
		mv.addObject("list", boardList);
		
		mv.setViewName("/notice/dataList");
		return mv;
	}

	@RequestMapping("/class/data/detail")
	@Override
	public ModelAndView getDetailView(ModelAndView mv, Board model) {
		// TODO Auto-generated method stub
		List<FileInfo> files = new ArrayList<FileInfo>();
		Menus menu = new Menus();
		
		RestTemplate rest = new RestTemplate();
		JSONObject json = new JSONObject(rest.getForObject(new String("http://jcoding.kr/api/board/detail/"+model.getId()), String.class));
		logger.info(json.toString());
		
		Board board = Board.parseBoard(json.getJSONObject("board"));
		
		mv.addObject("files", files);
		mv.addObject("menu", menu);
		mv.addObject("board", board);
		mv.addObject("listUrl", "/class/data/list");
		mv.setViewName("/notice/detailView");
		return mv;
	}
	@Override
	@RequestMapping(value="/class/data/edit", method = RequestMethod.GET)
	public ModelAndView getEditView(ModelAndView mv, Board model, HttpServletRequest request) {
		List<Menus> menus = menuService.selectChildren();
		mv.addObject("boardTypes", menus);
		
		RestTemplate rest = new RestTemplate();
		JSONObject json = new JSONObject(rest.getForObject(new String("http://jcoding.kr/api/board/detail/"+model.getId()), String.class));
		
		Board board = Board.parseBoard(json.getJSONObject("board"));
		mv.addObject("board", board);
		mv.setViewName("/notice/dataWrite");
		return mv;
	}
	@ResponseBody
	public String getEditView(Board model) {
		logger.info("edit");
		
		JSONObject json = new JSONObject();
		json.put("result", 1);
		return json.toString();
	}
	
	@ResponseBody
	@RequestMapping(value="/class/data/edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public String edit(Board model) {
		RestTemplate rest = new RestTemplate();
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append("http://jcoding.kr/api/board/edit/");
		
		JSONObject json = new JSONObject(rest.postForObject(urlBuilder.toString(), model, Board.class));
		
		return json.toString();
	}

	@Override
	@RequestMapping(value="/class/data/write", method = RequestMethod.GET)
	public ModelAndView getWriteView(ModelAndView mv, Board model, HttpServletRequest request, Integer type) {
		List<Menus> menus = menuService.selectChildren();
		mv.addObject("boardTypes", menus);
		mv.setViewName("/notice/dataWrite");
		return mv;
	}
	
	@Override
	@ResponseBody
	@RequestMapping(value="/class/data/write",
		method = RequestMethod.POST,
		produces = MediaType.APPLICATION_JSON_VALUE)
	public String write(Board model) {
		logger.info(model.toString());
		
		RestTemplate rest = new RestTemplate();
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append("http://jcoding.kr/api/board/write/");
		
		MultiValuedMap<String, String> param = new ArrayListValuedHashMap<String, String>();
		param.put("title",  model.getTitle());
		param.put("content", model.getContent());

		JSONObject json = new JSONObject(rest.postForObject(urlBuilder.toString(), model,  Board.class));
		return json.toString();
	}

	@Override
	public ModelAndView edit(Board model, ModelAndView mv) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@ResponseBody
	@RequestMapping(value="/class/data/delete",
					method = RequestMethod.POST,
					produces = MediaType.APPLICATION_JSON_VALUE)

	@Override
	public String delete(Board model) {
		logger.info(model.toString());
		RestTemplate rest = new RestTemplate();
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append("http://jcoding.kr/api/board/delete/");
		
//		MultiValuedMap<String, String> param = new ArrayListValuedHashMap<String, String>();
		JSONObject json = new JSONObject(rest.postForObject(urlBuilder.toString(), model, Board.class));
		logger.info(json.toString());
		return json.toString();
	}

}
