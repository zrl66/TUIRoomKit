<template>
  <div class="manage-member-container">
    <div class="manage-member-header">
      <div v-if="roomStore.isSpeakAfterTakingSeatMode" class="apply-count" @click="handleToggleStaged">
        <span :class="['apply-staged', {'apply-count-active': isOnStateTabActive }]">{{ alreadyStaged }}</span>
        <span :class="['apply-not-stage', { 'apply-count-active': !isOnStateTabActive }]">{{ notStaged }}</span>
      </div>
      <div v-if="applyToAnchorList.length > 0 && !isGeneralUser" class="apply-on-stage-info">
        <svg-icon style="display: flex" :icon="ApplyTipsIcon" class="apply-icon"></svg-icon>
        <div class="apply-info"> {{ applyToAnchorUserContent }} </div>
        <div class="apply-check" @click="showApplyUserList">{{ t('Check') }}</div>
      </div>
    </div>
    <div class="member-list-container">
      <div class="member-list-header">
        {{ t('Member List') }}
        <span class="member-count">({{ userNumber }}{{ t('members') }})</span>
      </div>
      <div class="member-list-container">
        <member-item v-for="(userInfo) in filteredUserList" :key="userInfo.userId" :user-info="userInfo"></member-item>
      </div>
    </div>
    <div v-if="!isGeneralUser" class="manage-member-bottom">
      <div
        class="manage-member-button"
        :class="isMicrophoneDisableForAllUser ? 'lift-all' : ''"
        @touchstart="toggleManageAllMember(ManageControlType.AUDIO)"
      >
        {{ audioManageInfo }}
      </div>
      <div
        class="manage-member-button"
        :class="isCameraDisableForAllUser ? 'lift-all' : ''"
        @touchstart="toggleManageAllMember(ManageControlType.VIDEO)"
      >
        {{ videoManageInfo }}
      </div>
      <div
        class="manage-member-button"
        @touchstart="toggleClickMoreBtn"
      >
        {{ t('More') }}
        <div v-show="showMoreControl" class="moreControl-container">
          <div class="moreControl-container-main">
            <div
              v-for="item in moreControlList"
              :key="item.type"
              class="user-operate-item"
              @touchstart="item.func(item.type)"
            >
              <svg-icon style="display: flex" :icon="item.icon"></svg-icon>
              <span class="operate-text">{{ item.title }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <Dialog
      v-model="showManageAllUserDialog"
      :title="dialogTitle"
      width="480px"
      :modal="true"
      :append-to-room-container="true"
      :confirm-button="t('Confirm')"
      :cancel-button="t('Cancel')"
      @confirm="doToggleManageAllMember"
      @cancel="showManageAllUserDialog = false"
    >
      <span>
        {{ dialogContent }}
      </span>
    </Dialog>
  </div>
</template>

<script setup lang='ts'>
import MemberItem from '../ManageMember/MemberItem/index.vue';
import Dialog from '../common/base/Dialog/index.vue';
import useIndex from './useIndexHooks';
import { storeToRefs } from 'pinia';
import { useRoomStore } from '../../stores/room';
import SvgIcon from '../common/base/SvgIcon.vue';
import ApplyTipsIcon from '../../assets/icons/ApplyTipsIcon.svg';
const roomStore = useRoomStore();
const {
  userNumber,
  applyToAnchorList,
  isMicrophoneDisableForAllUser,
  isCameraDisableForAllUser,
  isGeneralUser,
} = storeToRefs(roomStore);

const {
  audioManageInfo,
  videoManageInfo,
  showManageAllUserDialog,
  dialogTitle,
  dialogContent,
  ManageControlType,
  toggleManageAllMember,
  doToggleManageAllMember,
  t,
  alreadyStaged,
  notStaged,
  filteredUserList,
  isOnStateTabActive,
  handleToggleStaged,
  applyToAnchorUserContent,
  showApplyUserList,
  showMoreControl,
  moreControlList,
  toggleClickMoreBtn
} = useIndex();

</script>

<style lang="scss" scoped>
.manage-member-container {
  position: relative;
  height: 100%;
  display: flex;
  flex-direction: column;
  .manage-member-header {
    .apply-count {
      height: 36px;
      border-radius: 10px;
      margin: 16px 16px 8px 16px;
      background-color: var(--background-color-11);
      position: relative;
      display: flex;
      .apply-staged,
      .apply-not-stage {
        position: absolute;
        left: 0;
        top: 3px;
        width: 49%;
        height: 80%;
        filter: drop-shadow(0px 2px 4px rgba(32, 77, 141, 0.03)) drop-shadow(0px 6px 10px rgba(32, 77, 141, 0.06))
          drop-shadow(0px 3px 14px rgba(32, 77, 141, 0.05));
        border-radius: 10px;
        transform: translateX(4px);
        display: flex;
        justify-content: center;
        align-content: center;
        flex-wrap: wrap;
        color: var(--font-color-1);
        font-size: 14px;
        font-weight: 400;
      }
      .apply-not-stage {
        top: 50%;
        left: 50%;
        height: 30px;
        transform: translateY(-50%);
      }
      .apply-count-active {
        background-color: var(--background-color-12);
      }
    }
    .apply-on-stage-info {
      width: 100%;
      height: 40px;
      padding: 0 20px 0 26px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      .apply-icon {
        color: var(--font-color-2);
      }
      .apply-info {
        font-weight: 400;
        font-size: 14px;
        color: var(--font-color-8);
        padding-left: 4px;
        flex: 1;
      }
      .apply-check {
        text-align: center;
        line-height: 32px;
        font-weight: 400;
        font-size: 14px;
        color: var(--active-color-2);
        cursor: pointer;
      }
    }
    .apply-info {
      font-weight: 400;
      font-size: 14px;
      color: #FFFFFF;
    }
    .button {
      width: 82px;
      height: 32px;
      background: rgba(255,255,255,0.10);
      border: 1px solid #FFFFFF;
      border-radius: 2px;
      text-align: center;
      line-height: 32px;
      font-weight: 400;
      font-size: 14px;
      color: #FFFFFF;
      cursor: pointer;
    }
  }
  .member-list-container {
    flex: 1;
    min-height: 0;
    overflow-y: scroll;
    .member-count {
      margin-left: 5px;
    }
    &::-webkit-scrollbar {
      display: none;
    }
    .member-list-header {
      padding: 0 32px;
      margin-bottom: 8px;
      font-weight: 500;
      font-size: 14px;
      color: var(--input-font-color);
      line-height: 24px;
    }
    .member-list-content {
      flex: 1;
      overflow-y: scroll;
    }
  }
  .manage-member-bottom {
    width: 100%;
    display: flex;
    justify-content: space-around;
    z-index: 1;
    .manage-member-button {
      border-radius: 10px;
      padding: 13px 24px;
      background-color: var(--manage-member-button-h5);
      font-weight: 400;
      color: var(--mute-button-color-h5);
    }
    .lift-all {
      color: var(--mute-all-h5);
    }
  }
  .popup-dialog-audio,.popup-dialog-video {
    width: 64vw;
    background: var(--popup-mute-background-color-h5);
    border-radius: 8px;
    position: absolute;
    bottom: 46vh;
    left: 18vw;
    white-space: nowrap;
    .popup-dialog-audio-title {
      font-family: 'PingFang SC';
      font-style: normal;
      font-weight: 400;
      font-size: 16px;
      line-height: 22px;
      text-align: center;
      color: #2B2E38;
      white-space: normal;
      padding: 20px;
    }
    .popup-button{
      display: flex;
      justify-content: space-evenly;
      padding: 10px;
      .popup-button-cancel{
        padding: 4px 16px;
        background-color: #fff;
        color: #646366;
        border: 1px solid #dcdfe6;
        border-radius: 4px;
      }
      .popup-button-mute{
        padding: 4px 16px;
        background-color: #006eff;
        color: #fff;
        border: 1px solid #dcdfe6;
        border-radius: 4px;
      }
    }
  }
}
  .agree, .cancel{
    padding: 14px;
    width: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    color: var(--active-color-1);
    font-size: 16px;
    font-weight: 500;
  }
  .cancel{
    color: var(--font-color-4);
  }
  .moreControl-container{
    position: fixed;
    left: 0;
    top: 0;
    bottom: 0;
    width: 100vw;
    height: auto;
    box-sizing: border-box;
    background-color: var(--log-out-mobile);
    .moreControl-container-main{
      width: 100%;
      background: var(--background-color-1);
      border-radius: 15px 15px 0px 0px;
      position: fixed;
      bottom: 0;
      display: flex;
      flex-direction: column;
      animation-duration: 200ms;
      animation-name: popup;
      padding-bottom: 4vh;
      gap: 5px;
      padding: 50px 25px;
      @keyframes popup {
        from {
          transform-origin: bottom;
          transform: scaleY(0);
        }
        to {
          transform-origin: bottom;
          transform: scaleY(1);
        }
      }
    }
    .user-operate-item {
      cursor: pointer;
      color: var(--popup-title-color-h5);
      height: 20px;
      display: flex;
      align-items: center;
      .operate-text {
        font-family: PingFang SC;
        margin-left: 8px;
        font-size: 14px;
        white-space: nowrap;
        line-height: 22px;
        font-weight: 400;
      }
    }
  }
</style>
