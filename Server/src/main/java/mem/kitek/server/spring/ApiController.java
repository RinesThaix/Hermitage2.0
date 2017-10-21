package mem.kitek.server.spring;

import mem.kitek.server.Bootstrap;
import mem.kitek.server.api.SendPictureMethod;
import mem.kitek.server.commons.ApiError;
import mem.kitek.server.commons.ApiMethod;
import mem.kitek.server.commons.ArgumentNotSpecifiedException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Created by RINES on 20.10.17.
 */
@Controller
@CrossOrigin
@RequestMapping(value="/api")
public class ApiController {

    @RequestMapping(value="/", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ResponseBody
    public String error() {
        return ApiError.UNKNOWN_METHOD.toJson();
    }

    @RequestMapping(value="/sendPicture", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, method = RequestMethod.POST)
    @ResponseBody
    public String sendPicture(@RequestParam(required = false) MultipartFile[] image, @RequestParam(required = false) Integer cam_id) {
        if(image == null)
            return ApiError.ARGUMENT_NOT_SPECIFIED.toJson("image");
        if(cam_id == null)
            return ApiError.ARGUMENT_NOT_SPECIFIED.toJson("cam_id");
        if(image.length != 1)
            return ApiError.ILLEGAL_ARGUMENT_FORMAT.toJson();
        try {
            SendPictureMethod.process(image[0].getOriginalFilename(), image[0].getBytes(), cam_id);
        }catch(NumberFormatException ex) {
            return ApiError.ILLEGAL_ARGUMENT_FORMAT.toJson();
        }catch(Exception ex) {
            return ApiError.INTERNAL_EXCEPTION.toJson(ex.getMessage());
        }
        return "ok";
    }

    @RequestMapping(value="/{method:.+}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ResponseBody
    public String methodExecution(@PathVariable(required = false) String method, @RequestParam Map<String,String> params) {
        if(method == null)
            return error();
        ApiMethod amethod = ApiMethod.lookup(method);
        if(amethod == null)
            return error();
        try {
            return amethod.process(params);
        }catch(ArgumentNotSpecifiedException ex) {
            return ApiError.ARGUMENT_NOT_SPECIFIED.toJson(ex.getArgument());
        }catch(NumberFormatException ex) {
            return ApiError.ILLEGAL_ARGUMENT_FORMAT.toJson();
        }catch(Exception ex) {
            ex.printStackTrace();
            return ApiError.INTERNAL_EXCEPTION.toJson(ex.getMessage());
        }
    }

}
