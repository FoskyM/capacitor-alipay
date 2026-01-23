<p align="center">
  <img src="https://img.shields.io/npm/v/@foskym/capacitor-alipay?style=flat-square&color=00A1E9" alt="npm version" />
  <img src="https://img.shields.io/npm/dm/@foskym/capacitor-alipay?style=flat-square&color=00A1E9" alt="npm downloads" />
  <img src="https://img.shields.io/github/license/foskym/capacitor-alipay?style=flat-square" alt="license" />
</p>

<h1 align="center">Capacitor Alipay</h1>

<p align="center">
  <b>支付宝支付 Capacitor 插件</b><br/>
  <sub>Alipay Payment Plugin for Capacitor</sub>
</p>

<p align="center">
 使用最新的 SDK 引入方式，为 Capacitor 应用提供支付宝移动支付能力，简洁易用，安全可靠。<br/>
  <sub>Seamless Alipay mobile payment integration for Capacitor applications with the modern method to use SDK.</sub>
</p>

---

## 📦 安装 | Installation

```bash
npm install @foskym/capacitor-alipay
npx cap sync
```

---

## 🔧 配置 | Configuration

### Android

无需额外配置，插件已自动处理所需权限。

No additional configuration required. The plugin automatically handles necessary permissions.

---

## 🚀 快速开始 | Quick Start

### 1. 注册 AppId | Register AppId

建议在应用启动时调用，用于启用防黑产安全机制和优化支付体验。

It's recommended to call this at app startup to enable anti-fraud security and optimize payment experience.

> 在支付前注册商户appId，支付宝主要用于“防黑产”等增加支付安全性逻辑，以及对支付体验会有较明显优化（加快唤起支付速度，对支付成功率有正向影响）

文档说明(Docs): https://opendocs.alipay.com/open/00dn75?pathHash=22ed0058#%E5%95%86%E6%88%B7appId%E6%B3%A8%E5%86%8C

```typescript
import { Alipay } from '@foskym/capacitor-alipay';

// 应用启动时注册 | Register at app startup
await Alipay.register({
  appId: 'your_alipay_app_id'
});
```

### 2. 发起支付 | Initiate Payment

```typescript
import { Alipay } from '@foskym/capacitor-alipay';

// orderInfo 由服务端生成并签名
// orderInfo should be generated and signed by your server
const result = await Alipay.pay({
  orderInfo: 'your_order_info_string_from_server'
});

// 处理支付结果 | Handle payment result
switch (result.resultStatus) {
  case '9000':
    // 订单支付成功 | Payment successful
    console.log('支付成功');
    break;
  case '8000':
  case '6004':
    // 支付结果未知，需要查询订单状态 | Result unknown, need to check order status
    console.log('支付结果未知，请查询订单状态');
    break;
  case '4000':
    // 订单支付失败 | Payment failed
    console.log('支付失败:', result.memo);
    break;
  case '5000':
    // 重复请求 | Duplicate request
    console.log('重复请求');
    break;
  case '6001':
    // 用户中途取消 | User cancelled
    console.log('用户取消支付');
    break;
  case '6002':
    // 网络连接出错 | Network error
    console.log('网络错误，请重试');
    break;
  default:
    // 其它支付错误 | Other payment errors
    console.log('支付错误:', result.memo);
}
```

---

## 📖 API 文档 | API Reference

### register(options)

注册支付宝 appId，建议在应用启动时调用。

Register Alipay appId. Recommended to call at app startup.

```typescript
register(options: RegisterOptions) => Promise<void>
```

| 参数 Param | 类型 Type | 描述 Description |
|------------|-----------|------------------|
| `options` | `RegisterOptions` | 注册配置 / Registration options |

#### RegisterOptions

| 属性 Property | 类型 Type | 描述 Description |
|---------------|-----------|------------------|
| `appId` | `string` | 支付宝开放平台分配的 appId / AppId from Alipay Open Platform |

---

### pay(options)

发起支付宝支付。

Initiate Alipay payment.

```typescript
pay(options: PayOptions) => Promise<PayResult>
```

| 参数 Param | 类型 Type | 描述 Description |
|------------|-----------|------------------|
| `options` | `PayOptions` | 支付配置 / Payment options |

#### PayOptions

| 属性 Property | 类型 Type | 描述 Description |
|---------------|-----------|------------------|
| `orderInfo` | `string` | 服务端生成的订单信息字符串 / Order info string from server |

#### PayResult

| 属性 Property | 类型 Type | 描述 Description |
|---------------|-----------|------------------|
| `resultStatus` | `string` | 结果状态码 / Result status code |
| `result` | `string` | 结果数据 / Result data |
| `memo` | `string` | 结果备注 / Result memo |

---

## 📋 状态码说明 | Status Codes

| 状态码 Code | 含义 Meaning |
|-------------|--------------|
| `9000` | ✅ **订单支付成功** / Payment successful |
| `8000` | ⏳ **正在处理中**，支付结果未知（有可能已经支付成功），请查询商家订单列表中订单的支付状态 / Processing, result unknown (may have succeeded), check order status |
| `4000` | ❌ **订单支付失败** / Payment failed |
| `5000` | ⚠️ **重复请求** / Duplicate request |
| `6001` | 🚫 **用户中途取消** / User cancelled |
| `6002` | 🌐 **网络连接出错** / Network error |
| `6004` | ❓ **支付结果未知**（有可能已经支付成功），请查询商家订单列表中订单的支付状态 / Result unknown (may have succeeded), check order status |
| 其它 Other | ⚡ **其它支付错误** / Other payment errors |

---

## ⚠️ 注意事项 | Important Notes

1. **orderInfo 必须由服务端生成** | **orderInfo must be generated by server**

   订单信息字符串包含签名，必须在服务端使用私钥生成，切勿在客户端进行签名操作。

   The order info string contains signature and must be generated on server side with private key. Never sign on client side.

2. **支付结果以服务端为准** | **Server-side verification is authoritative**

   客户端返回的支付结果仅供参考，最终支付状态应以服务端收到的支付宝异步通知为准。

   Client-side payment result is for reference only. Final payment status should be based on Alipay's async notification to your server.

---

## 📄 许可证 | License

MIT License © 2026-present