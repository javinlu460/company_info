package com.company.controller.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.company.common.result.R;
import com.company.entity.CmsBanner;
import com.company.entity.CmsNews;
import com.company.entity.CmsProduct;
import com.company.entity.SysConfig;
import com.company.service.CmsBannerService;
import com.company.service.CmsNewsService;
import com.company.service.CmsProductService;
import com.company.service.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "前台首页")
@RestController
@RequestMapping("/web/home")
public class WebHomeController {

    private final CmsBannerService cmsBannerService;
    private final CmsProductService cmsProductService;
    private final CmsNewsService cmsNewsService;
    private final SysConfigService sysConfigService;

    public WebHomeController(CmsBannerService cmsBannerService,
                             CmsProductService cmsProductService,
                             CmsNewsService cmsNewsService,
                             SysConfigService sysConfigService) {
        this.cmsBannerService = cmsBannerService;
        this.cmsProductService = cmsProductService;
        this.cmsNewsService = cmsNewsService;
        this.sysConfigService = sysConfigService;
    }

    @ApiOperation("首页聚合数据")
    @GetMapping("/index")
    public R<?> index() {
        Map<String, Object> data = new HashMap<>();

        // 轮播图列表(status=1, 按sort排序)
        List<CmsBanner> banners = cmsBannerService.list(
                new LambdaQueryWrapper<CmsBanner>()
                        .eq(CmsBanner::getStatus, 1)
                        .orderByAsc(CmsBanner::getOrderNum));
        data.put("banners", banners);

        // 推荐产品(is_recommend=1, limit 8)
        List<CmsProduct> products = cmsProductService.list(
                new LambdaQueryWrapper<CmsProduct>()
                        .eq(CmsProduct::getIsRecommend, 1)
                        .eq(CmsProduct::getStatus, 1)
                        .orderByDesc(CmsProduct::getCreateTime)
                        .last("LIMIT 8"));
        data.put("products", products);

        // 最新新闻(status=1, limit 6)
        List<CmsNews> news = cmsNewsService.list(
                new LambdaQueryWrapper<CmsNews>()
                        .eq(CmsNews::getStatus, 1)
                        .orderByDesc(CmsNews::getCreateTime)
                        .last("LIMIT 6"));
        data.put("news", news);

        // 网站配置
        Map<String, String> config = new HashMap<>();
        List<SysConfig> allConfigs = sysConfigService.list();
        Map<String, String> configMap = new HashMap<>();
        for (SysConfig c : allConfigs) {
            if (c.getConfigKey() != null) {
                configMap.put(c.getConfigKey(), c.getConfigValue() != null ? c.getConfigValue() : "");
            }
        }
        config.put("siteName", configMap.getOrDefault("company_name", ""));
        config.put("logo", configMap.getOrDefault("company_logo", ""));
        config.put("phone", configMap.getOrDefault("company_phone", ""));
        config.put("siteDesc", configMap.getOrDefault("company_description", ""));
        data.put("config", config);

        return R.ok(data);
    }
}
