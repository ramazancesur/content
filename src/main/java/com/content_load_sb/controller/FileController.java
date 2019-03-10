package com.content_load_sb.controller;
/*

import com.content_load_sb.persist.entity.DataFile;
import com.content_load_sb.service.interfaces.IFileService;
import com.content_load_sb.utils.filetransfer.tcp.JavaWebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

*/
/**
 * Created by asd on 17.11.2017.
 *//*

@RestController
@RequestMapping("/file")
public class FileController extends BaseController<DataFile> {
    @Autowired
    private IFileService fileService;
    @Autowired
    private JavaWebClient webClient;

    @GetMapping("/webSocketStart")
    public ResponseEntity<Void> webSocketStart() {
        try {
            webClient.calculateAndSendData();
            return new ResponseEntity<Void>(HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
    }

    @Override
    @GetMapping("/getAll")
    public ResponseEntity<List<DataFile>> getAll() {
        return super.getAll();
    }

    @Override
    @GetMapping("/getAll/id")
    public ResponseEntity<DataFile> getDataById(@PathVariable("id") Long id) {
        return super.getDataById(id);
    }

    @Override
    @PutMapping
    public ResponseEntity<Boolean> addData(DataFile data) {
        return super.addData(data);
    }

    @Override
    @PostMapping
    public ResponseEntity<Boolean> updateData(DataFile data) {
        return super.updateData(data);
    }

    @Override
    @DeleteMapping
    public ResponseEntity<Boolean> deleteData(DataFile data) {
        return super.deleteData(data);
    }
}
*/
