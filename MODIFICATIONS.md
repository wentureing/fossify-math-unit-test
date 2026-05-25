# 修改记录
本项目基于 [Fossify Calculator](https://github.com/FossifyOrg/Calculator) 源码修改，遵循 **GNU General Public License v3.0** 开源协议发布。

## 基础信息
- 原始项目：https://github.com/FossifyOrg/Calculator
- 许可证：GPL-3.0
- 修改日期：2026-05-25
- 修改类型：新增单元测试 / 仪器化测试 配置、依赖、目录

## 详细修改内容
### 1. 配置文件修改（app/build.gradle.kts）
1. 新增 Robolectric 测试框架配置，指定**阿里云Maven镜像**作为依赖下载源
2. 新增单元测试依赖库：
   - JUnit 4.13.2（单元测试框架）
   - Mockk 1.13.8（模拟测试框架）
   - Robolectric 4.13（Android单元测试框架）
   - AndroidX Test Core 1.5.0（基础测试核心库）
3. 新增 Espresso 仪器化测试全套稳定版依赖：
   - androidx.test:core:1.5.0
   - androidx.test:runner:1.5.2
   - androidx.test:rules:1.5.0
   - androidx.test.ext:junit:1.1.5
   - androidx.test.espresso:espresso-core:3.5.1

### 2. 源码目录新增
在 `app/src/` 目录下新增两个测试目录：
- `test/`：单元测试目录（本地JVM测试）
- `androidTest/`：仪器化测试目录（设备/模拟器UI测试）

### 3. 版本管理文件修改（gradle/libs.versions.toml）
注释 Espresso 相关版本配置：
- 注释稳定版：`espressoCore = "3.5.1"`
- 注释测试版：`espressoCore = "3.7.0-alpha05"`

## 合规声明
本项目为原始项目的**衍生作品**，完整保留原始 GPL-3.0 许可证，所有修改内容开源、可追溯，允许任何人自由使用、修改、分发。



# Modification Record
This project is based on [Fossify Calculator](https://github.com/FossifyOrg/Calculator).
Licensed under **GNU General Public License v3.0**.

## Basic Info
- Original project: https://github.com/FossifyOrg/Calculator
- License: GPL-3.0
- Date modified: 2026-05-25
- Modified by: wentureing

## Changes
### 1. app/build.gradle.kts
1. Added Robolectric config with **Alibaba Cloud Maven mirror**
2. Added unit test dependencies:
   - JUnit 4.13.2
   - Mockk 1.13.8
   - Robolectric 4.13
   - AndroidX Test Core 1.5.0
3. Added **Espresso UI test dependencies (stable version)**:
   - androidx.test:core:1.5.0
   - androidx.test:runner:1.5.2
   - androidx.test:rules:1.5.0
   - androidx.test.ext:junit:1.1.5
   - androidx.test.espresso:espresso-core:3.5.1

### 2. New test folders
Added these folders under app/src/:
- test/ (unit tests)
- androidTest/ (UI tests on device/emulator)

### 3. gradle/libs.versions.toml
Commented out these lines:
- espressoCore = "3.5.1"
- espressoCore = "3.7.0-alpha05"

## License Notice
This is a derivative work. All original licenses and copyrights are kept.
All changes are open and traceable.