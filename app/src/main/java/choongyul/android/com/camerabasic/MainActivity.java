package choongyul.android.com.camerabasic;


import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    Button mBtnCamera, mBtnGallery;
    ImageView mImageView;
    Uri fileUri = null;

    private final int REQ_PERMISSION = 100; // 권한요청 코드
    private final int REQ_CAMERA = 101; // 카메라 요청 코드
    private final int REQ_GALLERY = 102; // 갤러리 요청 코드

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //1. 위젯 세팅
        setWidget();
        //2. 버튼 컨트롤러 비활성화
        buttonDisable();
        //3. 리스너 등록
        setListener();
        //4. 권한처리
        checkPermission();
    }

    // 버튼 비활성화
    private void buttonDisable() {

        mBtnCamera.setEnabled(false);

    }
    // 버튼 활성화
    private void buttonEnable() {
        mBtnCamera.setEnabled(true);

    }

    private void init() {
        // 프로그램 실행

        // 권한처리 활성화 되면 버튼 활성화
        buttonEnable();

    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if( PermissionControl.checkPermission(this, REQ_PERMISSION) ) {
                init();
            }
        } else {
            init();
        }
    }

    // 위젯 세팅
    private void setWidget() {
        mBtnCamera = (Button) findViewById(R.id.btnCamera);
        mBtnGallery = (Button) findViewById(R.id.btnGallery);
        mImageView = (ImageView) findViewById(R.id.imageView);
    }

    // 리스너 세팅
    private void setListener() {
        mBtnCamera.setOnClickListener(clickListener);
        mBtnGallery.setOnClickListener(clickListener);

    }

//    아래 주석처리한 부분은 수업중에 <누가> 버전에 오류가 발생하여 집어넣은 부분이다.
//    정리하자면 누가버전은 startActivityForResult 를 통해 카메라를 실행시켰을 때 data에 사진을 받아오지 못해서
//    미리 사진을 받아올 수 있는 공간을 만들어 주고 그곳에다가 저장을 했는데 이렇게 되면 문제가 되는게
//    나같은 경우 카메라에 진입하자마자 null 의 이미지 파일이 생겨서 자꾸 nCloud 에 파일을 업로드하려고한다.
//    사진을 찰칵 하고 저장하거나 안하거나 만들어놓은 공간에 사진이 접착되어 일반 카메라와 다르게 동작한다.
//    처음에는 저장된 사진을 지우는 방식으로 하려 했으나 카메라에 진입하자마자 null의 사진파일이 생기는게 이상하다고 생각되어
//    이전에 하던방식으로 진행하였다.


//      클릭리스너 정의
//    private View.OnClickListener clickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Intent intent;
//            switch (v.getId()){
//                case R.id.btnCamera:
//                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//                    // 롤리팝 이상 버전에서는 아래 코드를 반영해야 한다.
//                    // --- 카메라 촬영 후 미디어 컨텐트 uri 를 생성해서 외부저장소에 저장한다 ---
//                    if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
//                        // 저장할 미디어 속성을 정의하는 클래스
//                        ContentValues values = new ContentValues(1);
//                        // 속성 중에 파일의 종류를 정의
//                        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
//                        // 전역변수로 정의한 fileUri에 외부저장소 컨텐츠가 있는 Uri를 입시로 생성해서 넣어준다
//                        fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//                        // 위에서 생성한 fileUri를 사진저장공간으로 사용하겠다고 설정
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//                        // Uri에 일기와 쓰기 권한을 시스템에 요청
//                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                    }
//                    // --- 여기 까지 컨텐트 uri 강제세팅 ---
//
//                    startActivityForResult(intent, REQ_CAMERA);
//                    break;
//                case R.id.btnGallery:
//                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    intent.setType("image/*"); // 외부저장소에 있는 이미지만 가져오기 위한 필터링
//                    startActivityForResult( Intent.createChooser(intent, " Select Picture"), REQ_GALLERY);
//
//                    break;
//            }
//        }
//    };
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        Log.i("Camera","resultCode==============================="+resultCode);
//
//        switch(requestCode) {
//            case REQ_CAMERA:
//                if ( resultCode == RESULT_OK) { // 사진 확인처리됨 RESULT_OK = -1
//                    // 롤리팝 체크
//                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//                        Log.i("Camera", "data.getData()===============================" + data.getData());
//                        fileUri = data.getData();
//                    }
//                    Log.i("Camera", "fileUri===============================" + fileUri);
//                    if (fileUri != null) {
//                        // 글라이드로 이미지 세팅하면 자동으로 사이즈 조절
//                        Glide.with(this)
//                                .load(fileUri)
//                                .into(mImageView);
//                    } else {
//                        //Uri를 잘못갖고 올경우 터지면서 이쪽으로 진입
//
//                    }
//
//                } else {
//                    // resultCode 가 0이고 사진이 찍혔으면 uri 가 남는데
//                    // uri 가 있을 경우 삭제처리... 집에가서하기
//                    if ( resultCode == 0 && data != null) {
//                        fileUri = data.getData();
//                        File dummy = new File(String.valueOf(fileUri));
//
//                        dummy.delete();
//
//                    }
//
//                    Toast.makeText(this, "사진파일이 없습니다", Toast.LENGTH_LONG).show();
//                }
//            case REQ_GALLERY:
//                    if (resultCode == RESULT_OK) {
//                fileUri = data.getData();
//                Glide.with(this).load(fileUri).into(mImageView);
//            }
//            break;
//        }
//    }


    //22222222222222222222222222222222222222222222222222222222222222222222222222222 클릭리스너 정의
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()){
                case R.id.btnCamera:
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQ_CAMERA);
                    break;
                case R.id.btnGallery:
                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*"); // 외부저장소에 있는 이미지만 가져오기 위한 필터링
                    startActivityForResult( Intent.createChooser(intent, " Select Picture"), REQ_GALLERY);

                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case REQ_CAMERA:
                if ( resultCode == RESULT_OK && data != null) { // 사진 확인처리됨 RESULT_OK = -1
                    mImageView.setImageURI(data.getData());
                } else {
                    Toast.makeText(this, "사진파일이 없습니다", Toast.LENGTH_LONG).show();
                }
            case REQ_GALLERY:
                    if (resultCode == RESULT_OK) {
                fileUri = data.getData();
                Glide.with(this).load(fileUri).into(mImageView);
            }
            break;
        }
    }
    //22222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222





    // 2. 권한체크 후 콜백 - 사용자가 확인 후 시스템이 호출하는 함수
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if( requestCode == REQ_PERMISSION) {

            if( PermissionControl.onCheckResult(grantResults)) {
                init();
            } else {
                Toast.makeText(this, "권한을 허용하지 않으시면 프로그램을 실행할 수 없습니다.", Toast.LENGTH_SHORT).show();
                // 선택 : 1 종료, 2 권한체크 다시물어보기 할수도 있다. 일단은 끝내기
//                finish();
            }

        }
    }
}
