<template>
  <div
    v-show="visible"
    ref="dialogRef"
    v-tap="handleOverlayClick"
    class="overlay-container"
    :class="[modal && 'overlay']"
    :style="overlayContainerStyle"
  >
    <div class="dialog-container">
      <span class="dialog-title">{{ props.title }}</span>
      <div :class="[hasTitle ? 'dialog-content' : 'dialog-content-notitle']">
        <slot></slot>
      </div>
      <div class="dialog-footer">
        <div v-tap="handleCancel" class="cancel-button">
          {{ props.cancelButton }}
        </div>
        <div v-tap="handleConfirm" class="confirm-button">
          {{ props.confirmButton }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {
  ref,
  watch,
  computed,
  onBeforeUnmount,
  withDefaults,
  defineProps,
  defineEmits,
} from 'vue';
import useZIndex from '../../../../hooks/useZIndex';
import '../../../../directives/vTap';

const visible = ref(false);
const overlayContainerStyle = ref({});
const { nextZIndex } = useZIndex();
const dialogRef = ref();
const hasTitle = computed(() => props.title !== '');

interface Props {
  value: boolean;
  title?: string;
  confirmButton?: string;
  cancelButton?: string;
  closeOnClickModal?: boolean;
  appendToBody?: boolean;
  appendToRoomContainer?: boolean;
  modal?: boolean;
}
const props = withDefaults(defineProps<Props>(), {
  title: '',
  value: false,
  confirmButton: '',
  cancelButton: '',
  closeOnClickModal: true,
  appendToBody: false,
  appendToRoomContainer: false,
  modal: false,
});
const emit = defineEmits(['input', 'close', 'confirm', 'cancel']);

watch(
  () => props.value,
  val => {
    visible.value = val;
  }
);

watch(visible, val => {
  if (val) {
    overlayContainerStyle.value = { zIndex: nextZIndex() };
    if (props.appendToBody) {
      document.body.appendChild(dialogRef.value);
    } else if (props.appendToRoomContainer) {
      document.getElementById('roomContainer')?.appendChild(dialogRef.value);
    }
  }
});

function handleConfirm() {
  emit('confirm');
}
function handleCancel() {
  emit('cancel');
}

function handleClose() {
  visible.value = false;
  emit('input', false);
  emit('close');
}

function handleOverlayClick(event: any) {
  if (!props.closeOnClickModal) {
    return;
  }
  if (event.target !== event.currentTarget) {
    return;
  }
  handleClose();
}

onBeforeUnmount(() => {
  if (props.appendToBody || props.appendToRoomContainer) {
    dialogRef.value.parentNode.removeChild(dialogRef.value);
  }
});
</script>

<style lang="scss" scoped>
.overlay-container {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background-color: var(--uikit-color-black-4);

  .dialog-container {
    position: fixed;
    top: 50%;
    left: 50%;
    display: flex;
    flex-direction: column;
    min-width: 80vw;
    font-style: normal;
    border-radius: 8px;
    transform: translate(-50%, -50%);
    background-color: var(--bg-color-dialog);
    color: var(--text-color-primary);

    .dialog-title {
      box-sizing: border-box;
      display: inline-block;
      padding: 24px 24px 12px;
      padding-top: 14px;
      font-size: 16px;
      font-weight: 500;
      text-align: center;
    }

    .dialog-content {
      padding: 0 24px 20px;
      font-size: 14px;
      font-weight: 400;
      text-align: center;
      color: var(--text-color-secondary);
    }

    .dialog-content-notitle {
      padding: 40px;
      font-size: 14px;
      text-align: center;
    }

    .dialog-footer {
      display: flex;
      border-top: 1px solid var(--stroke-color-module);

      .confirm-button,
      .cancel-button {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 50%;
        padding: 14px;
        font-size: 16px;
        font-weight: 400;
        line-height: normal;
        text-align: center;
        color: var(--text-color-primary);
      }

      .confirm-button {
        color: var(--text-color-link);
        border-left: 1px solid var(--stroke-color-module);
      }
    }
  }
}
</style>
