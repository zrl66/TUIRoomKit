package com.tencent.liteav.tuiroom;

import static com.tencent.trtc.TRTCCloudDef.TRTC_AUDIO_QUALITY_SPEECH;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.liteav.basic.UserModel;
import com.tencent.liteav.basic.UserModelManager;
import com.tencent.liteav.tuiroom.model.TUIRoomCore;
import com.tencent.liteav.tuiroom.model.TUIRoomCoreDef;
import com.tencent.liteav.tuiroom.model.impl.TUIRoomCoreImpl;
import com.tencent.liteav.tuiroom.model.impl.base.TRTCLogger;
import com.tencent.liteav.tuiroom.ui.RoomMainActivity;
import com.tencent.qcloud.tuicore.TUILogin;
import com.tencent.tuikit.engine.room.TUIRoomEngineDef;

public class TUIRoomImpl extends TUIRoom {
    private static final String TAG = "TUIRoomImpl";

    public static final int VIDEO_QUALITY_HD   = 1;
    public static final int VIDEO_QUALITY_FAST = 2;

    private static TUIRoom sInstance;

    private Context         mContext;
    private TUIRoomListener mListener;

    public static TUIRoom sharedInstance(Context context) {
        if (sInstance == null) {
            synchronized (TUIRoomImpl.class) {
                if (sInstance == null) {
                    sInstance = new TUIRoomImpl(context);
                }
            }
        }
        return sInstance;
    }

    private TUIRoomImpl(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public void createRoom(int roomId, TUIRoomEngineDef.SpeechMode speechMode, boolean isOpenCamera,
                           boolean isOpenMicrophone) {
        Log.e("AAAAA", "TUIRoomImpl createRoom");
        if (mContext == null) {
            Log.e("AAAAA", "TUIRoomImpl createRoom 1");
            TRTCLogger.e(TAG, "context is null");
            if (mListener != null) {
                mListener.onRoomCreate(-1, "context is null");
            }
            return;
        }

        if (roomId == 0) {
            Log.e("AAAAA", "TUIRoomImpl createRoom 2");
            TRTCLogger.e(TAG, "roomId is empty");
            if (mListener != null) {
                mListener.onRoomCreate(-1, "roomId is empty");
            }
            return;
        }
         // todo
/*        if (!TUILogin.isUserLogined()) {
            Log.e("AAAAA", "TUIRoomImpl createRoom 3");
            TRTCLogger.e(TAG, "user not login");
            if (mListener != null) {
                mListener.onRoomCreate(-1, "user not login");
            }
            return;
        }*/

        if (speechMode == null) {
            Log.e("AAAAA", "TUIRoomImpl createRoom 4");
            speechMode = TUIRoomEngineDef.SpeechMode.APPLY;
        }

        final UserModelManager manager = UserModelManager.getInstance();
        final UserModel userModel = manager.getUserModel();

        String userId = userModel.userId;
        String userName = userModel.userName;
        String userAvatar = userModel.userAvatar;
        RoomMainActivity.enterRoom(mContext, true, roomId, speechMode, userId, userName, userAvatar,
                isOpenCamera, isOpenMicrophone, TRTC_AUDIO_QUALITY_SPEECH, VIDEO_QUALITY_HD);
    }

    @Override
    public void enterRoom(int roomId, boolean isOpenCamera, boolean isOpenMicrophone) {
        if (mContext == null) {
            TRTCLogger.e(TAG, "context is null");
            if (mListener != null) {
                mListener.onRoomEnter(-1, "context is null");
            }
            return;
        }


        if (!TUILogin.isUserLogined()) {
            TRTCLogger.e(TAG, "user not login");
            if (mListener != null) {
                mListener.onRoomEnter(-1, "user not login");
            }
            return;
        }
        String userId = TUILogin.getUserId();
        String userName = TUILogin.getNickName();
        String userAvatar = TUILogin.getFaceUrl();
        RoomMainActivity.enterRoom(mContext, false, roomId, null, userId, userName, userAvatar,
                isOpenCamera, isOpenMicrophone, TRTC_AUDIO_QUALITY_SPEECH, VIDEO_QUALITY_HD);
    }

    @Override
    public void setListener(TUIRoomListener listener) {
        mListener = listener;
    }
}
