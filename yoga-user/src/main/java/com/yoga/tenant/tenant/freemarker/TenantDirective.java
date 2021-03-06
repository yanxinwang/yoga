package com.yoga.tenant.tenant.freemarker;

import com.yoga.core.utils.NumberUtil;
import com.yoga.tenant.tenant.service.TenantService;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

@Component
public class TenantDirective implements TemplateDirectiveModel {

    @Autowired
    private TenantService tenantService;

    @Override
    public void execute(Environment env, Map map, TemplateModel[] models, TemplateDirectiveBody body) throws TemplateException, IOException {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        Long tid = NumberUtil.longValue(request.getParameter("tid"));
        if (tid == null) tid = 0L;
        TemplateModel model = (TemplateModel) map.get("tag");
        String tag = model.toString();
        String value = "";
        if (tenantService != null) {
            Map<String, Object> values = tenantService.settingMap(tid);
            value = values.get(tag).toString();
        }
        Writer writer = env.getOut();
        writer.write(value);
        if (body != null) {
            body.render(env.getOut());
        }
    }
}
