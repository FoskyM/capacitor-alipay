import { WebPlugin } from '@capacitor/core';

import type { AlipayPlugin, PayOptions, PayResult, RegisterOptions } from './definitions';

export class AlipayWeb extends WebPlugin implements AlipayPlugin {
  async register(_options: RegisterOptions): Promise<void> {
    throw this.unimplemented('Alipay register is not available on web.');
  }

  async pay(_options: PayOptions): Promise<PayResult> {
    throw this.unimplemented('Alipay pay is not available on web.');
  }
}
