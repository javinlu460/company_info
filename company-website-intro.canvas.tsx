import { Stack, Row, Grid, H1, H2, H3, Text, Divider, Card, CardHeader, CardBody, Stat, Table, Tag, Callout, useHostTheme, MetricsGrid } from 'qoder/canvas';

export default function CompanyWebsiteIntro() {
  const { tokens } = useHostTheme();

  return (
    <Stack gap={32} style={{ maxWidth: 900, margin: '0 auto', padding: '40px 20px' }}>
      {/* 封面 */}
      <Stack gap={12} style={{ textAlign: 'center', padding: '60px 0' }}>
        <H1>企业官网管理系统</H1>
        <Text size="large" tone="secondary">一站式企业官网解决方案 · 全面可配置化管理</Text>
        <Divider />
        <Text tone="secondary">让企业拥有专业的网络形象，无需技术背景即可管理网站内容</Text>
      </Stack>

      <Divider />

      {/* 系统概述 */}
      <Stack gap={12}>
        <H2>系统概述</H2>
        <Text>本系统是一套完整的企业官网解决方案，包含前台展示网站和后台管理系统两大模块。企业管理员可通过后台轻松管理网站所有内容，包括公司信息、产品中心、新闻动态、轮播图、在线留言等，无需任何技术背景即可维护和更新网站。</Text>
        <MetricsGrid
          columns={4}
          items={[
            { label: '前台页面', value: '6+', description: '完整企业官网页面' },
            { label: '后台功能', value: '10+', description: '全面管理模块' },
            { label: '配置项', value: '11', description: '网站全局配置' },
            { label: '权限控制', value: 'RBAC', description: '企业级权限体系' },
          ]}
        />
      </Stack>

      <Divider />

      {/* 前台展示功能 */}
      <Stack gap={16}>
        <H2>前台展示功能</H2>
        <Text tone="secondary">面向访客的企业形象展示，专业美观、响应式适配</Text>
        <Table
          headers={['功能模块', '功能说明', '亮点特性']}
          rows={[
            ['首页轮播', '全屏轮播Banner展示，支持自动播放与手动切换', '后台可配置图片、链接、排序'],
            ['产品中心', '按分类展示企业产品，支持树形分类筛选', '分页加载、详情页富文本展示'],
            ['新闻中心', '企业新闻动态发布，支持分类与置顶', '时间轴展示、分类筛选'],
            ['关于我们', '公司简介、企业文化、资质荣誉、公司愿景', '多Tab切换、富文本内容'],
            ['联系我们', '展示联系方式，支持在线留言提交', '表单验证、留言后台可查'],
            ['业务领域', '展示企业核心业务范围', '可配置化内容管理'],
          ]}
        />
      </Stack>

      <Divider />

      {/* 后台管理功能 */}
      <Stack gap={16}>
        <H2>后台管理功能</H2>
        <Text tone="secondary">功能全面的CMS管理系统，操作简单直观</Text>
        <Grid columns={2} gap={16}>
          <Card>
            <CardHeader>网站配置</CardHeader>
            <CardBody>
              <Stack gap={6}>
                <Text>• 公司名称 / Logo 配置</Text>
                <Text>• 联系电话 / 邮箱 / 地址 / 传真</Text>
                <Text>• 备案号配置</Text>
                <Text>• 公司简介 / 企业文化（富文本）</Text>
                <Text>• 资质荣誉 / 公司愿景（富文本）</Text>
              </Stack>
            </CardBody>
          </Card>
          <Card>
            <CardHeader>内容管理</CardHeader>
            <CardBody>
              <Stack gap={6}>
                <Text>• 轮播图管理（上传/排序/启禁用）</Text>
                <Text>• 产品分类管理（树形结构）</Text>
                <Text>• 产品管理（富文本详情/封面图）</Text>
                <Text>• 新闻分类管理</Text>
                <Text>• 新闻管理（置顶/发布/封面图）</Text>
              </Stack>
            </CardBody>
          </Card>
          <Card>
            <CardHeader>系统管理</CardHeader>
            <CardBody>
              <Stack gap={6}>
                <Text>• 用户管理（新增/编辑/禁用）</Text>
                <Text>• 角色管理（RBAC权限分配）</Text>
                <Text>• 菜单管理（动态路由/按钮权限）</Text>
                <Text>• 完整的权限控制体系</Text>
              </Stack>
            </CardBody>
          </Card>
          <Card>
            <CardHeader>留言管理</CardHeader>
            <CardBody>
              <Stack gap={6}>
                <Text>• 查看访客在线留言</Text>
                <Text>• 标记已读/未读状态</Text>
                <Text>• 留言统计与仪表盘展示</Text>
                <Text>• 支持删除管理</Text>
              </Stack>
            </CardBody>
          </Card>
        </Grid>
      </Stack>

      <Divider />

      {/* 技术优势 */}
      <Stack gap={16}>
        <H2>技术优势</H2>
        <Table
          headers={['技术特性', '说明', '价值']}
          rows={[
            ['前后端分离架构', 'Vue 3 + Spring Boot，前后端独立开发部署', '开发效率高，维护成本低'],
            ['响应式设计', '适配PC、平板、手机等多终端', '一套代码，全终端覆盖'],
            ['RBAC权限体系', '用户-角色-菜单三级权限控制', '安全可靠，精细化管理'],
            ['全配置化管理', '所有内容后台可编辑，无需开发介入', '降低运营成本，快速响应'],
            ['富文本编辑器', 'WangEditor 5 所见即所得编辑', '非技术人员也能轻松编辑内容'],
            ['JWT安全认证', '无状态Token认证，接口安全防护', '防止未授权访问'],
          ]}
        />
      </Stack>

      <Divider />

      {/* 系统亮点 */}
      <Stack gap={16}>
        <H2>系统亮点</H2>
        <Grid columns={4} gap={16}>
          <Card>
            <CardBody>
              <Stack gap={8} style={{ textAlign: 'center' }}>
                <H3>操作简单</H3>
                <Text size="small" tone="secondary">直观的管理界面，无需培训即可上手操作</Text>
              </Stack>
            </CardBody>
          </Card>
          <Card>
            <CardBody>
              <Stack gap={8} style={{ textAlign: 'center' }}>
                <H3>SEO友好</H3>
                <Text size="small" tone="secondary">语义化HTML结构，利于搜索引擎收录排名</Text>
              </Stack>
            </CardBody>
          </Card>
          <Card>
            <CardBody>
              <Stack gap={8} style={{ textAlign: 'center' }}>
                <H3>安全稳定</H3>
                <Text size="small" tone="secondary">Spring Security + JWT，企业级安全保障</Text>
              </Stack>
            </CardBody>
          </Card>
          <Card>
            <CardBody>
              <Stack gap={8} style={{ textAlign: 'center' }}>
                <H3>扩展性强</H3>
                <Text size="small" tone="secondary">模块化设计，可按需扩展新功能模块</Text>
              </Stack>
            </CardBody>
          </Card>
        </Grid>
      </Stack>

      <Divider />

      {/* 技术栈 */}
      <Stack gap={12}>
        <H2>技术栈</H2>
        <Row gap={8} style={{ flexWrap: 'wrap' }}>
          <Tag>Vue 3</Tag>
          <Tag>Vite 5</Tag>
          <Tag>Element Plus</Tag>
          <Tag>Spring Boot 2.7</Tag>
          <Tag>MyBatis-Plus</Tag>
          <Tag>MySQL 8.0</Tag>
          <Tag>Spring Security</Tag>
          <Tag>JWT</Tag>
          <Tag>WangEditor 5</Tag>
          <Tag>Pinia</Tag>
        </Row>
      </Stack>

      <Divider />

      {/* 联系合作 */}
      <Callout tone="info" title="期待与您合作">
        如您对本系统感兴趣，欢迎联系我们获取演示账号或进一步沟通定制需求。我们可根据企业实际需要进行功能扩展和界面定制，助力企业数字化转型。
      </Callout>

      <Text size="small" tone="secondary" style={{ textAlign: 'center', paddingTop: 20 }}>
        企业官网管理系统 · 产品介绍
      </Text>
    </Stack>
  );
}
