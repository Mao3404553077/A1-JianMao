# 作业任务清单（PROG2004 Assessment 1）

截止：**2026 年 11 月 16 日（周一）23:59**，占总成绩 **40%**，个人作业。

## 一、这次作业到底要交什么

| 要交的东西 | 具体要求 | 放在哪 |
| --- | --- | --- |
| Java 项目压缩包 | 命名为 `A1-你的MySCU用户名.zip`，包含全部源代码 | MySCU 提交框 |
| GitHub 仓库链接 | 仓库里要有持续、有意义的提交记录；私有仓库要邀请 tutor 和 unit assessor | 提交框的 comments |
| WakaTime 报告/截图 | 命名为 `A1-用户名-WakaTime.png/jpg/pdf` | MySCU 提交框 |
| 视频链接 | 最长 5 分钟，讲清四大支柱，上传 SCU OneDrive 并生成可访问链接 | 提交框的 comments |
| GenAI 声明 | 按 brief 给出的两段话选一段 | 提交框的 comments |

## 二、程序本身要做的六个部分

| 部分 | 要做什么 | 本次实现 |
| --- | --- | --- |
| Part 1 | 设计一个"通用医护人员"类：ID（只能是数字）、姓名、每周固定上班日；封装、校验、打印、多个构造方法 | `HealthProfessional`（抽象类，EnumSet 存上班日） |
| Part 2 | 至少 4 种具体类型，其中必须有全科医生 GP；复用父类、不重复代码、各自展示信息 | `GeneralPractitioner`、`Specialist`、`Nurse`、`Dietitian`，另加抽象层 `Doctor` |
| Part 3 | 在 main 里创建多个对象（3 个 GP + 每种 1–2 个），用统一类型处理并打印 | `AssignmentOne.demonstratePartThree` |
| Part 4 | 设计 Appointment：关联病人、医生、时间；可接任何医生类型；能排序；不能出现无效状态 | `Appointment`（`Comparable`、构造校验）、`Patient` |
| Part 5 | 管理预约集合：添加（校验、防重复占号）、按医生ID/病人手机号筛选、排序、取消，失败要返回警告 | `AppointmentManager`、`ClinicSchedule` |
| Part 6 | 在 main 里完整演示正常流程和错误流程 | `AssignmentOne.demonstratePartsFiveAndSix` |

评分权重：Part 1 占 10%，Part 2 占 20%，Part 3 占 10%，Part 4 占 15%，
Part 5 占 15%，Part 6 占 10%，视频 10%，GitHub/WakaTime/代码质量 10%。

## 三、除了写代码，还必须做的事

1. **WakaTime**：现在就在 IDE 里装插件并登录。时间无法事后补记，越早开始越真实。
2. **GitHub**：至少每天提交一次，提交信息写清楚改了什么。不要最后一天一次性上传。
3. **视频（≤5 分钟）**：对每个支柱按"说出名字 → 用自己的话解释 → 指出代码位置 →
   说明为什么这样设计"四步讲。脚本见 `docs/VIDEO_SCRIPT.md`。
4. **GenAI 声明**：brief 把这次作业定为 Level 2 —— 可以用 AI 查思路、辅助学习、
   帮忙调试，**不允许**用 AI 写代码、写注释、做视频。所以这份参考代码不能原样提交：
   请自己动手重写一遍、改成自己的写法，并如实声明。
5. **能讲清自己的代码**：老师可能要求面试式讲解，讲不出来会按学术不端处理。

## 四、剩余时间怎么安排（今天 11 月 12 日）

| 日期 | 要做的事 |
| --- | --- |
| 11/12（今天） | 装 WakaTime、建 GitHub 仓库；通读 `src` 下所有类，理解每个设计决定 |
| 11/13 | 自己重写 Part 1–2 的类（照着思路写，不要复制粘贴），提交 3–4 次 |
| 11/14 | 重写 `Patient`、`Appointment`、`ClinicSchedule`，跑通 Part 3–4 |
| 11/15 | 重写 `AppointmentManager` 与演示部分，跑通全部输出；录视频 |
| 11/16 | 补注释和 README、导出 WakaTime、打包 zip、填 GenAI 声明、提交（留出 2 小时余量） |

## 五、提交前自检

- [ ] 项目能从零编译运行：双击 `run.bat`，或在 VS Code 里跑 `Java: run` 任务
- [ ] 压缩包叫 `A1-用户名.zip`，里面没有 `out/`、`.class`、`.idea/`
- [ ] GitHub 链接可访问（私有仓库已加协作者）
- [ ] WakaTime 文件命名正确
- [ ] 视频 ≤5 分钟、链接在无痕窗口能打开
- [ ] GenAI 声明已填
- [ ] 每个类我都能不看笔记讲出来

详细英文版清单见 `docs/SUBMISSION_CHECKLIST.md`，提交步骤见 `docs/GIT_PLAN.md`。
