import Vue from '../utils/vue';

class VueTouch {
  public dom: HTMLElement;
  public callback: Function;
  public isMove: boolean;

  constructor(el: HTMLElement, binding: any) {
    this.dom = el;
    this.callback = binding.value;

    this.isMove = false;

    el?.addEventListener(
      'touchstart',
      () => {
        this.touchstart();
      },
      { passive: true }
    );

    el?.addEventListener(
      'touchmove',
      () => {
        this.touchmove();
      },
      { passive: true }
    );

    el?.addEventListener(
      'touchend',
      (event: TouchEvent) => {
        this.touchend(event);
      },
      { passive: true }
    );
  }

  touchstart() {
    this.isMove = false;
  }

  touchmove() {
    this.isMove = true;
  }

  touchend(event?: TouchEvent) {
    if (!this.isMove) {
      this.callback(event);
    }
  }
}

Vue.directive('tap', {
  bind(el: HTMLElement, binding: any) {
    return new VueTouch(el, binding);
  },
});
