package app.sunshine.android.example.com.drinkshop.Utils;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import app.sunshine.android.example.com.drinkshop.Interfaces.UploadCallBack;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class ProgressRequest extends RequestBody {
    File file;
    private static final int DEFAULT_BUFFER_LENGTH=4096;
    UploadCallBack uploadCallBack;

    public ProgressRequest(File file, UploadCallBack uploadCallBack) {
        this.file = file;
        this.uploadCallBack = uploadCallBack;
    }

    @Override
    public long contentLength() throws IOException {
        return file.length();
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return MediaType.parse("image/*");
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
           long fileLength=file.length();
           byte[] buffer=new byte[DEFAULT_BUFFER_LENGTH];
        FileInputStream inputStream=new FileInputStream(file);
        try{
            long uploaded=0;
            int read=0;
            Handler handler=new Handler(Looper.getMainLooper());
            while((read=inputStream.read(buffer))!=-1){

                handler.post(new progressUpdater(uploaded,fileLength));
                uploaded+=read;
                sink.write(buffer,0,read);
            }

        }
        finally {
            inputStream.close();
        }

    }
    class progressUpdater implements Runnable{
        long uploaded,fileLength;

        public progressUpdater(long uploaded, long fileLength) {
            this.uploaded = uploaded;
            this.fileLength = fileLength;
        }

        @Override
        public void run() {
         uploadCallBack.onProgressUpdate((int)(100*uploaded/fileLength));
        }
    }
}
