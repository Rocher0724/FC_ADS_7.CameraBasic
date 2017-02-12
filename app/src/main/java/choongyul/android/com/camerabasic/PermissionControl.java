package choongyul.android.com.camerabasic;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

/** 권한처리를 담당하는 클래스
 * 권한 변경시 PERMISSION_ARRAY 의 값만 변경 해주면 된다.
 * Created by myPC on 2017-02-10.
 */

public class PermissionControl {
    // 권한체크 수정
    // 1. 요청할 권한목록 작성
    // 1. 요청할 권한 목록 작성
    public static final String PERMISSION_ARRAY[] = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
            ,   Manifest.permission.CAMERA
            // 3개가될수도 있고 4개가될수도 있다.
    };


    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkPermission(Activity activity, int req_permission) {
        // 1.1 런타임 권한체크 (권한을 추가할때 1.2 목록작성과 2.1 권한체크에도 추가해야한다.)
        boolean permCheck = true;
        for(String perm : PERMISSION_ARRAY) {
            if ( activity.checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED ) {
                permCheck = false;
                break;
            }
        }

        // 1.2 퍼미션이 모두 true 이면 프로그램 실행
        if(permCheck) {
            return true;
        } else {
            // 1.3 퍼미션중에 false가 있으면 시스템에 권한요청
            activity.requestPermissions(PERMISSION_ARRAY, req_permission);
            return false;
        }
    }

    // 2. 권한체크 후 콜백 - 사용자가 확인 후 시스템이 호출하는 함수
    public static boolean onCheckResult(int[] grantResults) {

        boolean checkResult = true;
        // 권한 처리 결과 값을 반보문을 돌면서 확인한 후 하나라도 승인되지 않았다면 false를 리턴해준다.
        for(int result : grantResults) {
            if( result != PackageManager.PERMISSION_GRANTED) {
                checkResult = false;
                break;
            }
        }
        return checkResult;

    }

}
