[
<#if dataList?? && (dataList?size>0)>
    <#list dataList as data>
    {
        "specialName":"${data.subjectName!'-'}",
        "downloadId ":"${data.fileId!'-'}",
        "downloadTime":"${data.downloadTime!'-'}",
        "accountPeriod":"${data.fileDate!'-'}",
        "conditions":"${data.dimensionName!'-'}",
        "state":"${data.isAvailable!'-'}"
    }
    <#if data_has_next>,</#if>
    </#list>
<#else>
</#if>
]