# FC_ADS_7.CameraBasic
주요 센서인 카메라를 사용해보고 갤러리에서 선택된 이미지를 Glide를 통해 가져와보았습니다.

```

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
    
```