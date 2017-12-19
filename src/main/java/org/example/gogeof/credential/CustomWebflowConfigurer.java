package org.example.gogeof.credential;

import org.apereo.cas.web.flow.AbstractCasWebflowConfigurer;
import org.apereo.cas.web.flow.CasWebflowConstants;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.engine.ViewState;
import org.springframework.webflow.engine.builder.BinderConfiguration;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;

/**
 * 重新定义默认web流程
 */
public class CustomWebflowConfigurer extends AbstractCasWebflowConfigurer {
    public CustomWebflowConfigurer(FlowBuilderServices flowBuilderServices, FlowDefinitionRegistry flowDefinitionRegistry){
        super(flowBuilderServices, flowDefinitionRegistry);
    }

    @Override
    protected void doInitialize() throws Exception {
        final Flow flow = getLoginFlow();
        bindCredential(flow);
    }

    /**
     * 绑定输入信息
     * @param flow
     */
    private void bindCredential(Flow flow) {
        // 重写绑定自定义credential
        createFlowVariable(flow, CasWebflowConstants.VAR_ID_CREDENTIAL, CertKeyCredential.class);

        // 登录页绑定新参数
        final ViewState state = (ViewState) flow.getState(CasWebflowConstants.STATE_ID_VIEW_LOGIN_FORM);
        final BinderConfiguration cfg = getViewStateBinderConfiguration(state);

        // TODO 待与前端一块修改
        //　由于用户名以及密码已经绑定，所以只需对新加系统参数绑定即可
        cfg.addBinding(new BinderConfiguration.Binding(cfg.getBinding("username").getProperty(), cfg.getBinding("username").getConverter(), false));
        cfg.addBinding(new BinderConfiguration.Binding(cfg.getBinding("password").getProperty(), cfg.getBinding("password").getConverter(), false));
        cfg.addBinding(new BinderConfiguration.Binding("signCert", null, false));
        cfg.addBinding(new BinderConfiguration.Binding("cryptionCert", null, false));
        cfg.addBinding(new BinderConfiguration.Binding("sourceData", null, false));
        cfg.addBinding(new BinderConfiguration.Binding("signedData", null, false));
    }
}
