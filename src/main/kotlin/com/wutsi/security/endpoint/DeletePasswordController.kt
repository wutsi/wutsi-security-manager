package com.wutsi.security.endpoint

import com.wutsi.security.delegate.DeletePasswordDelegate
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
public class DeletePasswordController(
    public val `delegate`: DeletePasswordDelegate
) {
    @DeleteMapping("/v1/passwords/{id}")
    public fun invoke(@PathVariable(name = "id") id: Long) {
        delegate.invoke(id)
    }
}
